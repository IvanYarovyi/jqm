/**
 * Copyright © 2013 enioka. All rights reserved
 * Authors: Pierre COPPEE (pierre.coppee@enioka.com)
 * Contributors : Marc-Antoine GOUILLART (marc-antoine.gouillart@enioka.com)
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
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@Embeddable
public class History implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -5249529794213078668L;
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@Column(nullable=true)
	private Integer returnedValue;
	private Calendar jobDate;
	private Integer jobDefId;
	private Integer sessionId;
	@ManyToOne(fetch=FetchType.EAGER, targetEntity=com.enioka.jqm.jpamodel.Queue.class)
	private Queue queue;
	@Column(length=1000)
	private String msg;
	@OneToMany(fetch=FetchType.EAGER, targetEntity=com.enioka.jqm.jpamodel.Message.class, cascade=CascadeType.ALL, mappedBy="history")
	private List<Message> messages;
	@OneToOne(fetch=FetchType.LAZY, targetEntity=com.enioka.jqm.jpamodel.JobInstance.class)
	private JobInstance jobInstance;
	private Calendar enqueueDate;
	private Calendar executionDate;
	private Calendar endDate;
	private String userName;
	@ManyToOne(fetch=FetchType.EAGER, targetEntity=com.enioka.jqm.jpamodel.Node.class)
	private Node node;
	@OneToMany(orphanRemoval=true)
	@JoinColumn(name="history_parameter")
	private List<JobHistoryParameter> parameters;


	public Integer getId()
	{
		return id;
	}

	public void setId(final Integer id)
	{
		this.id = id;
	}
	/**
	 * @return the returnedValue
	 */
	public Integer getReturnedValue()
	{
		return returnedValue;
	}
	/**
	 * @param returnedValue the returnedValue to set
	 */
	public void setReturnedValue(final Integer returnedValue)
	{
		this.returnedValue = returnedValue;
	}
	/**
	 * @return the jobDate
	 */
	public Calendar getJobDate()
	{
		return jobDate;
	}
	/**
	 * @param jobDate the jobDate to set
	 */
	public void setJobDate(final Calendar jobDate)
	{
		this.jobDate = jobDate;
	}
	/**
	 * @return the msg
	 */
	public String getMsg()
	{
		return msg;
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(final String msg)
	{
		this.msg = msg;
	}

	public JobInstance getJobInstance()
	{
		return jobInstance;
	}

	public void setJobInstance(final JobInstance jobInstance)
	{
		this.jobInstance = jobInstance;
	}

	public Calendar getEnqueueDate()
	{
		return enqueueDate;
	}

	public void setEnqueueDate(final Calendar enqueueDate)
	{
		this.enqueueDate = enqueueDate;
	}

	public Calendar getExecutionDate()
	{
		return executionDate;
	}

	public void setExecutionDate(final Calendar executionDate)
	{
		this.executionDate = executionDate;
	}

	public Calendar getEndDate()
	{
		return endDate;
	}

	public void setEndDate(final Calendar endDate)
	{
		this.endDate = endDate;
	}


	public List<JobHistoryParameter> getParameters() {

		return parameters;
	}


	public void setParameters(final List<JobHistoryParameter> parameters) {

		this.parameters = parameters;
	}


	public List<Message> getMessages() {

		return messages;
	}


	public void setMessages(final List<Message> messages) {

		this.messages = messages;
	}

	public Integer getJobDefId()
	{
		return jobDefId;
	}

	public void setJobDefId(final Integer jobDefId)
	{
		this.jobDefId = jobDefId;
	}

	public Queue getQueue()
	{
		return queue;
	}

	public void setQueue(final Queue queue)
	{
		this.queue = queue;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(final String userName)
	{
		this.userName = userName;
	}

	public Node getNode()
	{
		return node;
	}

	public void setNode(final Node node)
	{
		this.node = node;
	}

	public Integer getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(final Integer sessionId)
	{
		this.sessionId = sessionId;
	}
}
