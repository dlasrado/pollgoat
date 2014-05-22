/**
* FaseConfig.java
*
* Provides a set of utility methods to read properties from configuration file
*
* Copyright(c) 2014 Equinix, Inc.  All Rights Reserved.
* This software is the proprietary information of Equinix.
*
*/
package common.util;

import java.util.List;

import play.Configuration;
import play.Play;

public class AppConfig {


        private static volatile AppConfig instance;
        private Configuration configuration;
        private AppConfig(){
                configuration=Play.application().configuration();
        }

        /**
         * Creates a single instance of this class
         * @return
         */
        public static AppConfig getInstance(){
                synchronized(AppConfig.class){
                        if(instance==null)
                                instance=new AppConfig();
                }
                return instance;
        }

        /**
         * Get the property for name
         * @param configKey
         * @return
         */
        public String get(String configKey){
                return configuration.getString(configKey);
        }
        
        /**
         * Get the property list for name
         * @param configKey
         * @return
         */
        public List<String> getArray(String configKey){
        	return configuration.getStringList(configKey);
        }

}