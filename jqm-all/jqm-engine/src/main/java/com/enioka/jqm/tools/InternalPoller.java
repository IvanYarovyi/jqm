package com.enioka.jqm.tools;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.enioka.jqm.jpamodel.Node;

class InternalPoller implements Runnable
{
    private static Logger jqmlogger = Logger.getLogger(InternalPoller.class);
    private boolean run = true;
    private JqmEngine engine = null;
    private EntityManager em = Helpers.getNewEm();
    private Node node = null;
    private Thread localThread = null;
    private long step = 10000;

    InternalPoller(JqmEngine e)
    {
        this.engine = e;
        this.node = em.find(Node.class, e.getNode().getId());
        this.step = Long.parseLong(Helpers.getParameter("internalPollingPeriodMs", String.valueOf(this.step), em));
    }

    void stop()
    {
        // The test is important: it prevents the engine from calling interrupt() when stopping
        // ... which can be triggered inside InternalPoller.run!
        if (this.run)
        {
            this.run = false;
            if (this.localThread != null)
            {
                this.localThread.interrupt();
            }
        }
    }

    @Override
    public void run()
    {
        this.localThread = Thread.currentThread();
        while (true)
        {
            try
            {
                Thread.sleep(this.step);
            }
            catch (InterruptedException e)
            {
            }
            if (!run)
            {
                break;
            }

            // Check if stop order
            em.refresh(this.node);
            if (this.node.isStop())
            {
                jqmlogger.debug("Node has received a stop order");
                jqmlogger.debug("At stop order time, there are " + this.engine.getCurrentlyRunningJobCount() + " jobs running in the node");
                this.run = false;
                this.engine.stop();
                break;
            }
        }

        jqmlogger.info("End of internal poller");
    }

}