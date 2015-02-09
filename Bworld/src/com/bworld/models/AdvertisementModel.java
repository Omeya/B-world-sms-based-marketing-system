package com.bworld.models;

import java.io.Serializable;

public class AdvertisementModel implements Serializable
{
	private String advertisementTitle;
	private String description;
	private String imageUrl;
	private String adId;
	private String smsCount;
	private String startTime;
	private String timestamp;
	private String endTime;
	
	
	public void setAdvertisementTitle(String advertisementText) {
		this.advertisementTitle = advertisementText;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public String getAdvertisementTitle() {
		return advertisementTitle;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setAdId(String adId) {
		this.adId = adId;
	}
	public String getAdId() {
		return adId;
	}
	public void setSmsCount(String smsCount) {
		this.smsCount = smsCount;
	}
	public String getSmsCount() {
		return smsCount;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getEndTime() {
		return endTime;
	}
	
	
}
