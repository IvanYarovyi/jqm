package com.enioka.jqm.api;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class ClientFactory implements IClientFactory
{
    private static JqmClient defaultClient;
    private static ConcurrentMap<String, JqmClient> clients = new ConcurrentHashMap<String, JqmClient>();

    @Override
    public JqmClient getClient()
    {
        return getClient(null, null, true);
    }

    @Override
    public JqmClient getClient(String name, Properties props, boolean cached)
    {
        if (props == null)
        {
            props = new Properties();
        }

        if (!cached)
        {
            return new JerseyClient(props);
        }

        synchronized (clients)
        {
            if (name == null)
            {
                if (defaultClient == null)
                {
                    defaultClient = new JerseyClient(props);
                }
                return defaultClient;
            }
            else
            {
                clients.putIfAbsent(name, new JerseyClient(props));
                return clients.get(name);
            }
        }
    }

    @Override
    public void resetClient(String name)
    {
        if (name != null)
        {
            synchronized (clients)
            {
                if (clients.containsKey(name))
                {
                    clients.get(name).dispose();
                    clients.remove(name);
                }
            }
        }
        else
        {
            synchronized (clients)
            {
                if (defaultClient != null)
                {
                    defaultClient.dispose();
                    defaultClient = null;
                }
            }
        }
    }

}