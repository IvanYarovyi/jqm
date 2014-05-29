package com.enioka.jqm.webui.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.enioka.jqm.jpamodel.DeploymentParameter;
import com.enioka.jqm.jpamodel.GlobalParameter;
import com.enioka.jqm.jpamodel.JndiObjectResource;
import com.enioka.jqm.jpamodel.JndiObjectResourceParameter;
import com.enioka.jqm.jpamodel.Node;
import com.enioka.jqm.jpamodel.Queue;
import com.enioka.jqm.webui.admin.dto.Frontier;
import com.enioka.jqm.webui.admin.dto.GlobalParameterDto;
import com.enioka.jqm.webui.admin.dto.JndiObjectResourceDto;
import com.enioka.jqm.webui.admin.dto.JndiObjectResourcePrmDto;
import com.enioka.jqm.webui.admin.dto.NodeDTO;
import com.enioka.jqm.webui.admin.dto.QueueDTO;
import com.enioka.jqm.webui.admin.dto.QueueMappingDTO;

@Path("/")
public class AdminService
{
    private static EntityManagerFactory emf;

    private synchronized static EntityManager getEm()
    {
        if (emf == null)
        {
            Properties p = new Properties();
            p.put("javax.persistence.nonJtaDataSource", "java:/comp/env/jdbc/jqm");
            emf = Persistence.createEntityManagerFactory("jobqueue-api-pu", p);
        }

        return emf.createEntityManager();
    }

    // ////////////////////////////////////////////////////////////////////////
    // Nodes
    // ////////////////////////////////////////////////////////////////////////

    @GET
    @Path("node")
    @Produces(MediaType.APPLICATION_JSON)
    public List<NodeDTO> getNodes()
    {
        List<NodeDTO> res = new ArrayList<NodeDTO>();

        EntityManager em = getEm();
        List<Node> r = em.createQuery("SELECT n FROM Node n", Node.class).getResultList();

        for (Node n : r)
        {
            res.add(Frontier.getDTO(n));
        }

        em.close();
        return res;
    }

    @GET
    @Path("node/{nodeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public NodeDTO getNode(@PathParam("nodeId") int nodeId)
    {
        EntityManager em = getEm();
        try
        {
            return Frontier.getDTO(em.find(Node.class, nodeId));
        }
        finally
        {
            em.close();
        }
    }

    // ////////////////////////////////////////////////////////////////////////
    // Queues
    // ////////////////////////////////////////////////////////////////////////

    @GET
    @Path("q")
    @Produces(MediaType.APPLICATION_JSON)
    public List<QueueDTO> getQueues()
    {
        List<QueueDTO> res = new ArrayList<QueueDTO>();

        EntityManager em = getEm();
        List<Queue> r = em.createQuery("SELECT n FROM Queue n", Queue.class).getResultList();

        for (Queue n : r)
        {
            res.add(Frontier.getDTO(n));
        }

        em.close();
        return res;
    }

    @GET
    @Path("q/{qId}")
    @Produces(MediaType.APPLICATION_JSON)
    public QueueDTO getQueue(@PathParam("qId") int qId)
    {
        EntityManager em = getEm();
        try
        {
            return Frontier.getDTO(em.find(Queue.class, qId));
        }
        finally
        {
            em.close();
        }
    }

    // Add one element to the collection => POST.
    @POST
    @Path("q")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setQueue(QueueDTO queue)
    {
        EntityManager em = getEm();
        try
        {
            em.getTransaction().begin();
            setQueue(em, queue);
            em.getTransaction().commit();
        }
        finally
        {
            em.close();
        }
    }

    @PUT
    @Path("q/{qId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setQueue(@PathParam("qId") Integer qId, QueueDTO queue)
    {
        queue.setId(qId);
        setQueue(queue);
    }

    // Replace collection => PUT
    @PUT
    @Path("q")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setQueues(List<QueueDTO> queues)
    {
        EntityManager em = getEm();
        try
        {
            em.getTransaction().begin();
            for (QueueDTO dto : queues)
            {
                setQueue(em, dto);
            }
            em.getTransaction().commit();
        }
        finally
        {
            em.close();
        }
    }

    // Must be called within a JPA transaction
    private Queue setQueue(EntityManager em, QueueDTO dto)
    {
        Queue n = null;

        if (dto.getId() == null)
        {
            n = new Queue();
        }
        else
        {
            n = em.find(Queue.class, dto.getId());
        }

        // Update or set fields
        n.setDefaultQueue(dto.isDefaultQueue());
        n.setDescription(dto.getDescription());
        n.setName(dto.getName());
        n.setTimeToLive(-1);

        // save
        em.merge(n);

        // Done
        return n;
    }

    @DELETE
    @Path("q/{qId}")
    public void deleteQueue(@PathParam("qId") Integer qId)
    {
        EntityManager em = getEm();
        Queue q = null;
        try
        {
            q = em.find(Queue.class, qId);
            em.getTransaction().begin();
            em.remove(q);
            em.getTransaction().commit();
        }
        finally
        {
            em.close();
        }
    }

    // ////////////////////////////////////////////////////////////////////////
    // Deployment parameters - queue mappings
    // ////////////////////////////////////////////////////////////////////////

    @GET
    @Path("qmapping")
    @Produces(MediaType.APPLICATION_JSON)
    public List<QueueMappingDTO> getQueueMappings()
    {
        List<QueueMappingDTO> res = new ArrayList<QueueMappingDTO>();
        EntityManager em = getEm();

        try
        {
            List<DeploymentParameter> r = em.createQuery("SELECT n FROM DeploymentParameter n", DeploymentParameter.class).getResultList();

            for (DeploymentParameter n : r)
            {
                res.add(Frontier.getDTO(n));
            }
            return res;
        }
        finally
        {
            em.close();
        }
    }

    @GET
    @Path("qmapping/{mId}")
    @Produces(MediaType.APPLICATION_JSON)
    public QueueMappingDTO getQueueMapping(@PathParam("mId") int mId)
    {
        EntityManager em = getEm();
        try
        {
            return Frontier.getDTO(em.find(DeploymentParameter.class, mId));
        }
        finally
        {
            em.close();
        }
    }

    // Must be called within a JPA transaction
    private DeploymentParameter setQueueMapping(EntityManager em, QueueMappingDTO dto)
    {
        DeploymentParameter n = null;

        if (dto.getId() == null)
        {
            n = new DeploymentParameter();
        }
        else
        {
            n = em.find(DeploymentParameter.class, dto.getId());
        }

        // Update or set fields
        n.setNbThread(dto.getNbThread());
        n.setNode(em.find(Node.class, dto.getNodeId()));
        n.setPollingInterval(dto.getPollingInterval());
        n.setQueue(em.find(Queue.class, dto.getQueueId()));

        // save
        em.merge(n);

        // Done
        return n;
    }

    @PUT
    @Path("qmapping")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setQueueMappings(List<QueueMappingDTO> dtos)
    {
        EntityManager em = getEm();
        try
        {
            em.getTransaction().begin();
            for (QueueMappingDTO dto : dtos)
            {
                setQueueMapping(em, dto);
            }
            em.getTransaction().commit();
        }
        finally
        {
            em.close();
        }
    }

    // ////////////////////////////////////////////////////////////////////////
    // JNDI
    // ////////////////////////////////////////////////////////////////////////

    @GET
    @Path("jndi")
    @Produces(MediaType.APPLICATION_JSON)
    public List<JndiObjectResourceDto> getJndiResources()
    {
        List<JndiObjectResourceDto> res = new ArrayList<JndiObjectResourceDto>();
        EntityManager em = getEm();

        try
        {
            List<JndiObjectResource> r = em.createQuery("SELECT n FROM JndiObjectResource n", JndiObjectResource.class).getResultList();
            for (JndiObjectResource n : r)
            {
                res.add(Frontier.getDTO(n));
            }
            return res;
        }
        finally
        {
            em.close();
        }
    }

    @GET
    @Path("jndi/{aId}")
    @Produces(MediaType.APPLICATION_JSON)
    public JndiObjectResourceDto getJndiResource(@PathParam("aId") Integer aId)
    {
        EntityManager em = getEm();
        try
        {
            return Frontier.getDTO(em.find(JndiObjectResource.class, aId));
        }
        finally
        {
            em.close();
        }
    }

    private JndiObjectResource setJndiResource(EntityManager em, JndiObjectResourceDto dto)
    {
        JndiObjectResource jpa = null;

        if (dto.getId() == null)
        {
            jpa = new JndiObjectResource();
        }
        else
        {
            jpa = em.find(JndiObjectResource.class, dto.getId());
        }

        jpa.setAuth(dto.getAuth());
        jpa.setDescription(dto.getDescription());
        jpa.setFactory(dto.getFactory());
        jpa.setName(dto.getName());
        jpa.setSingleton(dto.getSingleton());
        jpa.setType(dto.getType());

        jpa = em.merge(jpa);

        List<JndiObjectResourceParameter> prmFromBefore = new ArrayList<JndiObjectResourceParameter>();
        List<JndiObjectResourceParameter> prmNow = new ArrayList<JndiObjectResourceParameter>();
        for (JndiObjectResourceParameter ee : jpa.getParameters())
        {
            prmFromBefore.add(ee);
        }

        for (JndiObjectResourcePrmDto p : dto.getParameters())
        {
            if (p.getKey() == null || p.getKey().isEmpty() || p.getValue() == null || p.getValue().isEmpty())
            {
                continue;
            }

            JndiObjectResourceParameter np = null;
            if (p.getId() == null)
            {
                np = new JndiObjectResourceParameter();
            }
            else
            {
                np = em.find(JndiObjectResourceParameter.class, p.getId());
            }
            np.setKey(p.getKey());
            np.setValue(p.getValue());
            np.setResource(jpa);
            np = em.merge(np);
            prmNow.add(np);
        }

        // Remove parameters that are not present anymore
        ml: for (JndiObjectResourceParameter presentbefore : prmFromBefore)
        {
            for (JndiObjectResourceParameter stillhere : prmNow)
            {
                if (stillhere.getId() == presentbefore.getId())
                {
                    continue ml;
                }
            }
            jpa.getParameters().remove(presentbefore);
            em.remove(presentbefore);
        }

        return jpa;
    }

    // Add one element to the collection => POST.
    @POST
    @Path("jndi")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setJndiResource(JndiObjectResourceDto queue)
    {
        EntityManager em = getEm();
        try
        {
            em.getTransaction().begin();
            setJndiResource(em, queue);
            em.getTransaction().commit();
        }
        finally
        {
            em.close();
        }
    }

    // ////////////////////////////////////////////////////////////////////////
    // Global parameters
    // ////////////////////////////////////////////////////////////////////////

    // Must be called within a JPA transaction
    private GlobalParameter setGlobalParameter(EntityManager em, GlobalParameterDto dto)
    {
        GlobalParameter n = null;

        if (dto.getId() == null)
        {
            n = new GlobalParameter();
        }
        else
        {
            n = em.find(GlobalParameter.class, dto.getId());
        }

        // Update or set fields
        n.setKey(dto.getKey());
        n.setValue(dto.getValue());

        // save
        n = em.merge(n);

        // Done
        return n;
    }

    @GET
    @Path("prm")
    @Produces(MediaType.APPLICATION_JSON)
    public List<GlobalParameterDto> getGlobalParameters()
    {
        List<GlobalParameterDto> res = new ArrayList<GlobalParameterDto>();
        EntityManager em = getEm();

        try
        {
            List<GlobalParameter> r = em.createQuery("SELECT n FROM GlobalParameter n", GlobalParameter.class).getResultList();
            for (GlobalParameter n : r)
            {
                res.add(Frontier.getDTO(n));
            }
            return res;
        }
        finally
        {
            em.close();
        }
    }

    @PUT
    @Path("prm")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setGlobalParameters(List<GlobalParameterDto> dtos)
    {
        EntityManager em = getEm();
        try
        {
            List<GlobalParameter> existBefore = em.createQuery("SELECT n FROM GlobalParameter n", GlobalParameter.class).getResultList();
            List<GlobalParameter> existAfter = new ArrayList<GlobalParameter>();

            em.getTransaction().begin();

            // Update or create items
            for (GlobalParameterDto dto : dtos)
            {
                existAfter.add(setGlobalParameter(em, dto));
            }

            // Delete old items
            old: for (GlobalParameter before : existBefore)
            {
                for (GlobalParameter after : existAfter)
                {
                    if (before.getId().equals(after.getId()))
                    {
                        continue old;
                    }
                }
                em.remove(before);
            }

            // Done
            em.getTransaction().commit();
        }
        finally
        {
            em.close();
        }
    }

    @POST
    @Path("prm")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setGlobalParameter(GlobalParameterDto dto)
    {
        EntityManager em = getEm();
        try
        {
            em.getTransaction().begin();
            setGlobalParameter(em, dto);
            em.getTransaction().commit();
        }
        finally
        {
            em.close();
        }
    }

    @PUT
    @Path("prm/{pId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setGlobalParameter(@PathParam("pId") Integer pId, GlobalParameterDto dto)
    {
        dto.setId(pId);
        setGlobalParameter(dto);
    }

    @GET
    @Path("prm/{pId}")
    @Produces(MediaType.APPLICATION_JSON)
    public GlobalParameterDto getGlobalParameter(@PathParam("pId") int pId)
    {
        EntityManager em = getEm();
        try
        {
            return Frontier.getDTO(em.find(GlobalParameter.class, pId));
        }
        finally
        {
            em.close();
        }
    }

    @DELETE
    @Path("prm/{pId}")
    public void deleteGlobalParameter(@PathParam("pId") Integer pId)
    {
        EntityManager em = getEm();
        GlobalParameter q = null;
        try
        {
            q = em.find(GlobalParameter.class, pId);
            em.getTransaction().begin();
            em.remove(q);
            em.getTransaction().commit();
        }
        finally
        {
            em.close();
        }
    }
}