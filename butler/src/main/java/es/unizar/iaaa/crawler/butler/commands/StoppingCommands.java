package es.unizar.iaaa.crawler.butler.commands;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Stopping commands. This class contains every command which stops the crawling system or a part of
 * it
 */
@Component
public class StoppingCommands implements CommandMarker {

    private static final Logger LOGGER = Logger.getLogger(StoppingCommands.class);

    @Autowired
    private Operations ops;

    @CliAvailabilityIndicator({"stopCrawl"})
    public boolean stopNutchAvailable() {
        // always available
        return true;
    }

    @CliAvailabilityIndicator({"stopContainer"})
    public boolean stopContainerAvailable() {
        // always available
        return true;
    }

    @CliAvailabilityIndicator({"deleteContainer"})
    public boolean deleteContainerAvailable() {
        // always available
        return true;
    }

    @CliAvailabilityIndicator({"pauseContainer"})
    public boolean pauseContainerAvailable() {
        // always available
        return true;
    }

    @CliAvailabilityIndicator({"deleteImage"})
    public boolean deleteImageAvailable() {
        // always available
        return true;
    }

    /**
     * Stop the crawl in the docker container
     */
    @CliCommand(value = "stopCrawl", help = "Stops the crawler int the docker container")
    public String stopNutch(

            @CliOption(key = {"idUser"}, mandatory = true, help = "id of the user") final String idUser,
            @CliOption(key = {"idCrawl"}, mandatory = true, help = "id of the new crawler") final String idCrawl) {
        String id = idUser + "_" + idCrawl;
        // docker exec $idContainer kill -9 $(docker exec $idContainer
        // ps |
        // grep crawl | awk '{print $1;}')
        // docker exec $idContainer kill -9 $(docker exec $idContainer
        // ps |
        // grep sh | awk '{print $1;}')
        // docker exec $idContainer kill -9 $(docker exec $idContainer
        // ps |
        // grep java | awk '{print $1;}')
        String command = "docker exec  " + id + " ps";
        String process;
        if (!ops.containerExists(idUser, idCrawl)) {
            return "The container where it is trying to stop the crawl don't exist";
        }
        if (ops.containerRunning(idUser, idCrawl)) {
            try (BufferedReader out = ops.executeCommand(command, false)) {
                while ((process = out.readLine()) != null) {
                    // Para todos los procesos busca los procesos a eliminar
                    if (process.contains("crawl") || process.contains("java") || process.contains("java")) {
                        LOGGER.info(process);
                        command = " docker exec  " + id + " kill -9 " + process.split(" ")[1];
                        LOGGER.info(command);
                        ops.executeCommand(command, true);
                        return "Crawl stopped correctly";

                    }
                }
            } catch (IOException e) {
                LOGGER.warn("IOException: " + e.getMessage(), e);
                return "Docker container don't exist, please, try executing the start command";
            }
        }
        return "Docker container has to be running in order to be able to extract the information";
    }


    /**
     * Pause the docker container
     */
    @CliCommand(value = "pauseContainer", help = "Pause the docker Container")
    public String pauseContainer(

            @CliOption(key = {"idUser"}, mandatory = true, help = "id of the user") final String idUser,
            @CliOption(key = {"idCrawl"}, mandatory = true, help = "id of the new crawler") final String idCrawl) {
        String id = idUser + "_" + idCrawl;
        // docker pause $idContainer
        String command = "docker pause " + id;
        if (!ops.containerExists(idUser, idCrawl)) {
            return "The container trying to pause don't exist";
        }
        if (!ops.containerRunning(idUser, idCrawl)) {
            return "The container trying to pause is not up";
        }
        try {
            ops.executeCommand(command, true);

        } catch (IOException e) {
            LOGGER.warn("IOException: " + e.getMessage(), e);
            return "Docker container don't exist, please, try executing the start command";
        }
        return "Container paused correctly";
    }


    /**
     * Stop the docker container
     */
    @CliCommand(value = "stopContainer", help = "stops the docker Container")
    public String stopContainer(

            @CliOption(key = {"idUser"}, mandatory = true, help = "id of the user") final String idUser,
            @CliOption(key = {
                    "time"}, mandatory = false, specifiedDefaultValue = "1", help = "time in seconds (waiting until shutting down)") String time,
            @CliOption(key = {"idCrawl"}, mandatory = true, help = "id of the new crawler") final String idCrawl) {
        String id = idUser + "_" + idCrawl;
        if (time == null)
            time = "1";
        // docker stop -t $tiempo $idContainer
        String command = "docker stop -t " + time + " " + id;
        if (!ops.containerExists(idUser, idCrawl)) {
            return "The container trying to stop don't exist";
        }
        if (!ops.containerRunning(idUser, idCrawl)) {
            return "The container trying to stop is not up";
        }
        try {
            ops.executeCommand(command, true);

        } catch (IOException e) {
            LOGGER.warn("IOException: " + e.getMessage(), e);
            return "Docker container don't exist, please, try executing the start command";
        }
        return "Container stopped correctly";
    }

    /**
     * delete the docker container
     */
    @CliCommand(value = "deleteContainer", help = "deletes the docker Container")
    public String deleteContainer(

            @CliOption(key = {"idUser"}, mandatory = true, help = "id of the user") final String idUser,
            @CliOption(key = {"idCrawl"}, mandatory = true, help = "id of the new crawler") final String idCrawl) {
        String id = idUser + "_" + idCrawl;
        // docker stop -t $tiempo $idContainer
        String command = "docker rm " + id;
        if (!ops.containerExists(idUser, idCrawl)) {
            return "The container trying to delete don't exist";
        }
        if (!ops.containerStopped(idUser, idCrawl)) {
            return "The container has to be stopped in order to deleted it";
        }
        try {
            ops.executeCommand(command, true);
        } catch (IOException e) {
            LOGGER.warn("IOException: " + e.getMessage(), e);
            return "Docker container don't exist, please, try executing the start command";
        }
        return "Container deleted correctly";
    }

    /**
     * delete the docker image
     */
    @CliCommand(value = "deleteImage", help = "deletes the docker Image")
    public String deleteImage(

            @CliOption(key = {"idUser"}, mandatory = true, help = "id of the user") final String idUser,
            @CliOption(key = {"idCrawl"}, mandatory = true, help = "id of the new crawler") final String idCrawl) {
        String id = idUser + "_" + idCrawl;
        // docker stop -t $tiempo $idContainer
        String comando = "docker rmi " + id;

        if (!ops.imageExists(idUser, idCrawl)) {
            return "The image don't exists";

        }
        if (ops.containerExists(idUser, idCrawl)) {
            return "The image couldn't be deleted due to a container of this image exists";
        }
        try {
            ops.executeCommand(comando, true);
        } catch (IOException e) {
            LOGGER.warn("IOException: " + e.getMessage(), e);
            return "Docker image don't exist, please, try executing the start command";
        }
        return "Image delete correctly";
    }

}