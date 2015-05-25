/**
 * <h1>Template Processor API</h1>
 * Template processors in the classpath may be annotated with
 * {@link eu.unipv.epsilon.enigma.template.api.Template @Template}; its event handlers must be annotated with
 * {@link eu.unipv.epsilon.enigma.template.api.Template.EventHandler @Template.EventHandler} and accept one of
 * the following events:
 * <ul>
 *     <li>{@link eu.unipv.epsilon.enigma.template.api.DocumentGenerationEvent DocumentGenerationEvent}</li>
 * </ul>
 */
package eu.unipv.epsilon.enigma.template.api;