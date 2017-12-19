package ua.nure.sokolov.practice8.entity;

public class Group {
    private int groupId;
    private String name;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupId=" + groupId +
                ", name='" + name + '\'' +
                '}';
    }

    public static Group createGroup(String s) {
        Group group = new Group();
        group.setName(s);
        return group;
    }
}
