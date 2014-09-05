package cz.vysinsky.circleguard.model.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Build {

    public static final int STATUS_GREEN = 1;
    public static final int STATUS_RED = 2;
    public static final int STATUS_BLUE = 3;
    public static final int STATUS_ORANGE = 4;
    public static final int STATUS_GRAY = 5;

    @JsonProperty("build_num")
    private Integer id;

    @JsonProperty("reponame")
    private String repositoryName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("status")
    private String status;

    @JsonProperty("build_time_millis")
    private Long buildTime;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("branch")
    private String branch;

    @JsonProperty("ssh_enabled")
    private Boolean sshEnabled;

    @JsonProperty("start_time")
    private Date startTime;

    @JsonProperty("stop_time")
    private Date stopTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(Long buildTime) {
        this.buildTime = buildTime;
    }

    public int getStatus() {
        if (status.equals("success") || status.equals("fixed")) {
            return STATUS_GREEN;
        }
        if (status.equals("failed") || status.equals("timedout")) {
            return STATUS_RED;
        }

        if (status.equals("scheduled") || status.equals("queued")) {
            return STATUS_ORANGE;
        }

        if (status.equals("running")) {
            return STATUS_BLUE;
        }

        return STATUS_GRAY;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Boolean getSshEnabled() {
        return sshEnabled;
    }

    public void setSshEnabled(Boolean sshEnabled) {
        this.sshEnabled = sshEnabled;
    }

    public String getElapsedTime() {

        Long bt;

        if (buildTime == null) {

            if (startTime == null) {
                return "00:00";
            }

            Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            bt = now.getTime().getTime() - startTime.getTime();
        } else {
            bt = buildTime;
        }

        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(bt),
                TimeUnit.MILLISECONDS.toSeconds(bt) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(bt))
        );
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getStatusText() {
        return status;
    }
}
