/**
 * Copyright © 2013 enioka. All rights reserved
 * Authors: Marc-Antoine GOUILLART (marc-antoine.gouillart@enioka.com)
 *          Pierre COPPEE (pierre.coppee@enioka.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.enioka.jqm.jpamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * <strong>Not part of any API - this an internal JQM class and may change without notice.</strong> <br>
 * JPA persistence class for storing the definition of all the user codes (the payloads) that can be run by the JQM engines. It contains all
 * the metadata needed to create an execution request (a {@link JobInstance}).
 */
@Entity
@Table(name = "JobDef")
public class JobDef implements Serializable
{
    private static final long serialVersionUID = -3276834475433922990L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "description", length = 1024)
    private String description;

    @Column(name = "canBeRestarted")
    private boolean canBeRestarted = true;

    @Column(nullable = false, length = 100, name = "javaClassName")
    private String javaClassName;

    @Column(length = 1024, name = "filePath")
    private String filePath;

    @ManyToOne(optional = false)
    @JoinColumn(name = "queue_id")
    private Queue queue;

    @Column(name = "maxTimeRunning")
    private Integer maxTimeRunning;

    @Column(nullable = false, name = "applicationName", unique = true, length = 100)
    private String applicationName;

    @Column(length = 50, name = "application")
    private String application;

    @Column(length = 50, name = "module")
    private String module;

    @Column(length = 50, name = "keyword1")
    private String keyword1;

    @Column(length = 50, name = "keyword2")
    private String keyword2;

    @Column(length = 50, name = "keyword3")
    private String keyword3;

    @Column(name = "highlander", nullable = false)
    private boolean highlander = false;

    @Column(name = "jarPath", length = 1024)
    private String jarPath;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "java_opts", length = 200)
    private String javaOpts;

    @Column(name = "external", nullable = false)
    private boolean external = false;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "JobDefId")
    private List<JobDefParameter> parameters = new ArrayList<JobDefParameter>();

    /**
     * A technical ID without any meaning. Generated by the database.
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * True if instances of this {@link JobDef} can be restarted (i.e. run with exactly the same parameters and context). Default is true.
     */
    public boolean isCanBeRestarted()
    {
        return canBeRestarted;
    }

    /**
     * See {@link #isCanBeRestarted()}
     */
    public void setCanBeRestarted(final boolean canBeRestarted)
    {
        this.canBeRestarted = canBeRestarted;
    }

    /**
     * The "main" class of the payload. I.e. the class containing a static main function, or implementing {@link Runnable}.<br>
     * Must be a fully qualified name.<br>
     * Max length is 100.
     */
    public String getJavaClassName()
    {
        return javaClassName;
    }

    /**
     * See {@link #getJavaClassName()}
     */
    public void setJavaClassName(final String javaClassName)
    {
        this.javaClassName = javaClassName;
    }

    /**
     * @deprecated was never used and never will be (this would be impossible to implement in a predictable fashion).
     */
    public Integer getMaxTimeRunning()
    {
        return maxTimeRunning;
    }

    /**
     * @deprecated was never used and never will be (this would be impossible to implement in a predictable fashion).
     */
    public void setMaxTimeRunning(final Integer maxTimeRunning)
    {
        this.maxTimeRunning = maxTimeRunning;
    }

    /**
     * The applicative key of the {@link JobDef}. {@link JobDef} are always retrieved through this name.<br>
     * Max length is 100.
     */
    public String getApplicationName()
    {
        return applicationName;
    }

    /**
     * See {@link #getApplicationName()}
     */
    public void setApplicationName(final String applicationName)
    {
        this.applicationName = applicationName;
    }

    /**
     * An optional classification tag (default is NULL).<br>
     * Max length is 50.
     */
    public String getApplication()
    {
        return application;
    }

    /**
     * See {@link #getApplication()}
     */
    public void setApplication(final String application)
    {
        this.application = application;
    }

    /**
     * An optional classification tag (default is NULL).<br>
     * Max length is 50.
     */
    public String getModule()
    {
        return module;
    }

    /**
     * See {@link #getModule()}
     */
    public void setModule(final String module)
    {
        this.module = module;
    }

    /**
     * An optional classification tag (default is NULL).<br>
     * Max length is 50.
     */
    public String getKeyword1()
    {
        return keyword1;
    }

    /**
     * See {@link #getKeyword1()}
     */
    public void setKeyword1(final String keyword1)
    {
        this.keyword1 = keyword1;
    }

    /**
     * An optional classification tag (default is NULL).<br>
     * Max length is 50.
     */
    public String getKeyword2()
    {
        return keyword2;
    }

    /**
     * See {@link #getKeyword2()}
     */
    public void setKeyword2(final String keyword2)
    {
        this.keyword2 = keyword2;
    }

    /**
     * An optional classification tag (default is NULL).<br>
     * Max length is 50.
     */
    public String getKeyword3()
    {
        return keyword3;
    }

    /**
     * See {@link #getKeyword3()}
     */
    public void setKeyword3(final String keyword3)
    {
        this.keyword3 = keyword3;
    }

    /**
     * Set to true to enable Highlander mode: never more than one concurrent execution of the same {@link JobDef} inside the whole cluster.
     * Default is false.
     */
    public boolean isHighlander()
    {
        return highlander;
    }

    /**
     * See {@link #isHighlander()}
     */
    public void setHighlander(final boolean highlander)
    {
        this.highlander = highlander;
    }

    /**
     * The {@link Queue} on which the instances created from this {@link JobDef} should run. This is only the "default" queue - it may be
     * overloaded inside the execution request.
     */
    public Queue getQueue()
    {
        return queue;
    }

    /**
     * See {@link #getQueue()}
     */
    public void setQueue(final Queue queue)
    {
        this.queue = queue;
    }

    /**
     * Used to contain the path to the directory containing the jar file.<br>
     * Max length is 1024.
     * 
     * @deprecated jarPath contains the full path.
     */
    public String getFilePath()
    {
        return filePath;
    }

    /**
     * Used to contain the path to the directory containing the jar file.
     * 
     * @deprecated jarPath contains the full path.
     */
    public void setFilePath(final String filePath)
    {
        this.filePath = filePath;
    }

    /**
     * The path of the jar file containing the payload to run. The path must be relative to the job repository root ({@link Node#getRepo()}
     * ).<br>
     * Max length is 1024.
     */
    public String getJarPath()
    {
        return jarPath;
    }

    /**
     * See {@link #getJarPath()}
     */
    public void setJarPath(final String jarPath)
    {
        this.jarPath = jarPath;
    }

    /**
     * Parameters (i.e. key/value pairs) that should be present for all instances created from this JobDef. This list may be empty.<br>
     * These are only the "default" parameters - each parameter may be overloaded inside the execution request (which may even specify
     * parameters which are not present in the default parameters).
     */
    public List<JobDefParameter> getParameters()
    {
        return parameters;
    }

    /**
     * See {@link #getParameters()}
     */
    public void setParameters(final List<JobDefParameter> parameters)
    {
        this.parameters = parameters;
    }

    /**
     * A (compulsory) description of what this paylod does.<br>
     * Max length is 1024.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * See {@link #getDescription()}
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * The options passed to the JVM when launching this job definition. Only used if {@link #isExternal()} is <code>true</code>.<br>
     * These options are split on spaces and passed individually to the JVM.<br>
     * If <code>null</code>, the global parameter <code>defaultExternalOpts</code> is used. It this parameter is null too, default values
     * are used.
     */
    public String getJavaOpts()
    {
        return javaOpts;
    }

    /**
     * See {@link #getJavaOpts()}
     */
    public void setJavaOpts(String javaOpts)
    {
        this.javaOpts = javaOpts;
    }

    /**
     * If true, the instances created from this JobDef will be run inside a dedicated JVM instead of simply being a thread inside an engine.
     * Default is <code>false</code>.<br>
     * If using this, JVM options specific to this JobDef may be set through {@link #getJavaOpts()}.
     */
    public boolean isExternal()
    {
        return external;
    }

    /**
     * See {@link #isExternal()}
     */
    public void setExternal(boolean external)
    {
        this.external = external;
    }

    /**
     * If <code>false</code>, the instances created from this JobDef won't actually run: the engine will simply fake a successful run.<br>
     * Default is <code>true</code>
     */
    public boolean isEnabled()
    {
        return enabled;
    }

    /**
     * See {@link #isEnabled()}
     */
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
