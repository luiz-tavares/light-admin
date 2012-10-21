package org.lightadmin.core.config.beans.support;

import org.junit.Test;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.configuration.EntityConfiguration;
import org.lightadmin.core.config.domain.context.ScreenContext;
import org.lightadmin.core.config.domain.filter.Filters;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.scope.Scopes;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.util.ClassUtils;

import static org.junit.Assert.*;
import static org.lightadmin.core.test.util.BeanDefinitionUtils.constructorArgValue;
import static org.lightadmin.core.test.util.BeanDefinitionUtils.propertyValue;
import static org.lightadmin.core.test.util.DummyConfigurationsHelper.*;

public class ConfigurationClassToBeanDefinitionTransformerTest {

	private ConfigurationClassToBeanDefinitionTransformer subject = ConfigurationClassToBeanDefinitionTransformer.INSTANCE;

	@Test( expected = IllegalArgumentException.class )
	public void nullConfigurationNotAllowed() {
		subject.apply( null );
	}

	@Test( expected = IllegalArgumentException.class )
	public void missingAdministrationAnnotation() {
		subject.apply( WrongConfiguration.class );
	}

	@Test
	public void emptyConfigurationBeanDefinitionCreation() {
		final BeanDefinition beanDefinition = subject.apply( DomainEntityEmptyConfiguration.class );

		assertNotNull( beanDefinition );
	}

	@Test
	public void beanDefinitionHasCorrectBeanClass() {
		final BeanDefinition beanDefinition = subject.apply( DomainEntityEmptyConfiguration.class );

		assertEquals( DomainTypeAdministrationConfiguration.class.getName(), beanDefinition.getBeanClassName() );
	}

	@Test
	public void beanDefinitionInitializedWithDomainType() {
		final BeanDefinition beanDefinition = subject.apply( DomainEntityEmptyConfiguration.class );

		assertEquals( DomainEntity.class, constructorArgValue( beanDefinition, 0 ) );
	}

	@Test
	public void beanDefinitionInitializedWithRepositoryBeanReference() {
		final BeanDefinition beanDefinition = subject.apply( DomainEntityEmptyConfiguration.class );

		assertEquals( RuntimeBeanReference.class, constructorArgValue( beanDefinition, 1 ).getClass() );
	}

	@Test
	public void emptyEntityConfigurationCreatedIfNotDefined() throws Exception {
		final BeanDefinition beanDefinition = subject.apply( DomainEntityEmptyConfiguration.class );

		assertDefaultConfigurationUnitCreated( beanDefinition, "entityConfiguration", EntityConfiguration.class );
	}

	@Test
	public void emptyScreenContextCreatedIfNotDefined() throws Exception {
		final BeanDefinition beanDefinition = subject.apply( DomainEntityEmptyConfiguration.class );

		assertDefaultConfigurationUnitCreated( beanDefinition, "screenContext", ScreenContext.class );
	}

	@Test
	public void emptyListViewFragmentCreatedIfNotDefined() throws Exception {
		final BeanDefinition beanDefinition = subject.apply( DomainEntityEmptyConfiguration.class );

		assertDefaultConfigurationUnitCreated( beanDefinition, "listViewFragment", Fragment.class );
	}

	@Test
	public void emptyScopesCreatedIfNotDefined() throws Exception {
		final BeanDefinition beanDefinition = subject.apply( DomainEntityEmptyConfiguration.class );

		assertDefaultConfigurationUnitCreated( beanDefinition, "scopes", Scopes.class );
	}

	@Test
	public void emptyFiltersCreatedIfNotDefined() throws Exception {
		final BeanDefinition beanDefinition = subject.apply( DomainEntityEmptyConfiguration.class );

		assertDefaultConfigurationUnitCreated( beanDefinition, "filters", Filters.class );
	}

	@Test
	public void emptyConfigurationUnitCreatedInCaseOfException() {
		final BeanDefinition beanDefinition = subject.apply( ConfigurationWithException.class );

		assertDefaultConfigurationUnitCreated( beanDefinition, "entityConfiguration", EntityConfiguration.class );
	}

	private static void assertDefaultConfigurationUnitCreated( BeanDefinition beanDefinition, String configurationPropertyName, Class configurationUnitClass ) {
		final Object configurationUnit = propertyValue( beanDefinition, configurationPropertyName );

		assertNotNull( configurationUnit );
		assertTrue ( ClassUtils.isAssignableValue(configurationUnitClass, configurationUnit) );
	}
}