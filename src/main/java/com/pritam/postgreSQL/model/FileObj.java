package com.pritam.postgreSQL.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "files")
public class FileObj implements Serializable {

	@Id
	@GeneratedValue(generator = "file_generator")
	@SequenceGenerator(
			name = "file_generator",
			sequenceName = "file_generator",
			initialValue = 1
			)
	private Long id;

	@NotBlank
	@Size(min = 3, max = 100)
	private String filename;

	@NotBlank
	private String filepath;

	@NotBlank
	private String filedatatype;

	@NotBlank
	private String filesize;


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, updatable = false)
	@CreatedDate
	private Date createdAt = new Date();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getFiledatatype() {
		return filedatatype;
	}

	public void setFiledatatype(String filedatatype) {
		this.filedatatype = filedatatype;
	}

	public String getFilesize() {
		return filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


}
