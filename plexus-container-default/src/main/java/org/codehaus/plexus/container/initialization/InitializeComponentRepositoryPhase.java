package org.codehaus.plexus.container.initialization;

/*
 * Copyright 2001-2006 Codehaus Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.codehaus.plexus.component.repository.ComponentRepository;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.io.PlexusTools;
import org.codehaus.plexus.component.repository.exception.ComponentRepositoryException;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.PlexusConfigurationException;

/**
 * @author Jason van Zyl
 */
public class InitializeComponentRepositoryPhase
    extends AbstractCoreComponentInitializationPhase
{
    public void initializeCoreComponent( ContainerInitializationContext context )
        throws ContainerInitializationException
    {
        ComponentRepository componentRepository = context.getContainerConfiguration().getComponentRepository();

        // Add the components defined in the container xml configuration
        try
        {
            PlexusConfiguration configuration = context.getContainerXmlConfiguration();

            PlexusConfiguration[] componentConfigurations = configuration.getChild( "components" ).getChildren( "component" );
            for ( PlexusConfiguration componentConfiguration : componentConfigurations )
            {
                ComponentDescriptor<?> componentDescriptor = PlexusTools.buildComponentDescriptor( componentConfiguration );
                componentDescriptor.setRealm( context.getContainer().getContainerRealm() );
                componentRepository.addComponentDescriptor( componentDescriptor );
            }
        }
        catch ( PlexusConfigurationException e )
        {
            throw new ContainerInitializationException( "Error initializing component repository: " +
                "Cannot unmarshall component descriptor: ", e );
        }
        catch ( ComponentRepositoryException e )
        {
            throw new ContainerInitializationException( "Error initializing component repository: ", e );
        }

        context.getContainer().setComponentRepository( componentRepository );
    }
}
