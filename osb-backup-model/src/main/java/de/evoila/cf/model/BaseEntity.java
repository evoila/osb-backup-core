/**
 * 
 */
package de.evoila.cf.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Christian Brinker, evoila.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface BaseEntity<ID extends Serializable> {

	public ID getId();
}
