package Implementations;

import Interfaces.Plugin;
import Interfaces.PluginManager;
import Implementations.plugins.*;
/*
import main.pluginManager.plugins.NaviPlugin;
import main.pluginManager.plugins.TemperaturePlugin;
import main.pluginManager.plugins.ToLowerPlugin;
*/
import Implementations.RequestImpl;

import java.util.LinkedList;
import java.util.List;

public class PluginManagerImpl implements PluginManager {

    private List<Plugin> pluginList = new LinkedList<>();

    public PluginManagerImpl(){
        //pluginList.add(new PluginImpl());
        pluginList.add(new ToLower());
        pluginList.add(new Temperature());
        pluginList.add(new Navi());
    }

    /**
     * Returns a list of all plugins. Never returns null.
     * TODO: Refactor to List<Plugin>, Enumeration is deprecated
     * @return
     */
    @Override
    public List<Plugin> getPlugins(){
        return pluginList;
    };


    /**
     * Adds a new plugin. If the plugin was already added, nothing will happen.
     * @param plugin
     */
    @Override
    public void add(Plugin plugin){
        if(!pluginList.contains(plugin)){
            pluginList.add(plugin);
        }
    };

    /**
     * Adds a new plugin by class name. If the plugin was already added, nothing will happen.
     * Throws an exeption, when the type cannot be resoled or the class does not implement Plugin.
     * @param plugin
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Override
    public void add(String plugin) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
        Class<?> classInstance = Class.forName(plugin);

        if(PluginImpl.class.isAssignableFrom(classInstance)){
            try {
                PluginImpl pluginInstance = (PluginImpl) classInstance.getDeclaredConstructor().newInstance();
                pluginList.add(pluginInstance);
            } catch (Exception exception){
                throw new ClassNotFoundException("Class not found");
            }
        } else {
            throw new ClassNotFoundException("Class not found");
        }
    };


    /**
     * Clears all plugins
     */
    @Override
    public void clear(){
        pluginList.clear();
    };
}
