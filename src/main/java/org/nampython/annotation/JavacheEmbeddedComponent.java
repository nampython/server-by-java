package org.nampython.annotation;

import com.cyecize.ioc.annotations.AliasFor;
import com.cyecize.ioc.annotations.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Alias annotation for {@link Service} which is used to map services that are only
 * intended for Javache Embedded.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AliasFor(Service.class)
public @interface JavacheEmbeddedComponent {

}
