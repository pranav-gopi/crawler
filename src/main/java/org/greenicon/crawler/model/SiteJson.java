package org.greenicon.crawler.model;

import java.util.Map;

public class SiteJson {

	public class Config {
		private String url;
		private String name;
		private String subname;
		private String cronExpression;

		public String getName() {
			return name;
		}

		public String getUrl() {
			return url;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getSubname() {
			return subname;
		}

		public void setSubname(String subname) {
			this.subname = subname;
		}

		public String getCronExpression() {
			return cronExpression;
		}

		public void setCronExpression(String cronExpression) {
			this.cronExpression = cronExpression;
		}

		@Override
		public String toString() {
			return "Config [url=" + url + ", name=" + name + ", subname=" + subname + "]";
		}

	}

	public class Create {
		private Map<String, Map<String, String>> tables;

		public Map<String, Map<String, String>> getTables() {
			return tables;
		}

		public void setTables(Map<String, Map<String, String>> tables) {
			this.tables = tables;
		}

		@Override
		public String toString() {
			return "Create [tables=" + tables + "]";
		}

	}

	public class Process {
		private String root;

		public String getRoot() {
			return root;
		}

		public void setRoot(String root) {
			this.root = root;
		}

		@Override
		public String toString() {
			return "Process [root=" + root + "]";
		}

	}

	private Config config;

	private Process process;

	private Map<String, Map<String, String>> create;

	public Config getConfig() {
		return config;
	}

	public Process getProcess() {
		return process;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public Map<String, Map<String, String>> getCreate() {
		return create;
	}

	public void setCreate(Map<String, Map<String, String>> create) {
		this.create = create;
	}

	@Override
	public String toString() {
		return "SiteJson [config=" + config + ", process=" + process + ", create=" + create + "]";
	}

}
