package ua.nure.sokolov.practice8.entity;

public class UserGroup {
    private int userId;
    private int groupId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "UserGroup{" +
                "userId=" + userId +
                ", groupId=" + groupId +
                '}';
    }
}
