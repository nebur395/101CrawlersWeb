/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package Errors;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * 
 * @author shathe
 *
 *Default exceptions for errors
 */
@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)  
public class ErrorInternal extends RuntimeException {

	public ErrorInternal(String msg) {
		super (msg);
	}
	
}