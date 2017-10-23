package modals;

public class WorkLog
{
    private final int workLogId;

    private final Employee employee;

    private final BusinessClient client;

    private final long startTime;

    private final long endTime;

    private final String description;

    public WorkLog(final int workLogId, final Employee employee, final BusinessClient client,
                   final long startTime, final long endTime, final String description) throws IllegalArgumentException
    {
        if (endTime < startTime)
        {
            throw new IllegalArgumentException("End time must be after start time");
        }
        this.workLogId = workLogId;
        this.employee = employee;
        this.client = client;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    public int getWorkLogId()
    {
        return this.workLogId;
    }

    public Employee getEmployee()
    {
        return this.employee;
    }

    public BusinessClient getClient()
    {
        return this.client;
    }

    public long getStartTime()
    {
        return this.startTime;
    }

    public long getEndTime()
    {
        return this.endTime;
    }

    public String getDescription()
    {
        return this.description;
    }
}
