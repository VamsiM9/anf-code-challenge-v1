package com.anf.core.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vamsi mandalapu
 *
 */
public class NewsObject {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private String author;

	private String content;

	private String description;

	private String title;

	private String url;

	private String urlImage;

	public String getAuthor() {
		return author;
	}

	public String getContent() {
		return content;
	}

	public String getDescription() {
		return description;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

}