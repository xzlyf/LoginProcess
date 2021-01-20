package com.xz.xlogin.entity;

import java.util.Date;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/1/16
 */
public class Event {

	/**
	 * 事件id
	 */
	private String id;
	/**
	 * 短标题
	 * 限制64字节
	 */
	private String shortTitle;

	/**
	 * 事件类容
	 * 限制512字节
	 */
	private String content;

	/**
	 * 是否已完成
	 */
	private boolean done;

	/**
	 * 提醒时间
	 */
	private Date remindTime;

	/**
	 * 创建事件
	 */
	private Date createTime;

	/**
	 * 最后一次更新时间
	 */
	private Date updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public Date getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(Date remindTime) {
		this.remindTime = remindTime;
	}
}
