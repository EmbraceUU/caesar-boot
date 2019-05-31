package com.demo.designpatterns.builderPattern;

/**
 * 建造者模式
 *
 * 分为指挥者类  建造者接口  建造者类   产品类
 *
 * 主要在于将复杂的构建过程与对象的表示分离成一个Builder类;
 * Builder会返回一个组装好的对象
 *
 * 比如 StringBuilder 和 String 的关系
 * append就是构建过程   最后返回一个String
 */
public class Project {

    /**
     * 私有属性
     */
    private final String projectName;
    private final String projectCode;
    private final String projectFuture;

    /**
     * 构造函数不要求为私有
     *
     */
    Project(ProjectBuilder builder){
        this.projectCode = builder.projectCode;
        this.projectName = builder.projectName;
        this.projectFuture = builder.projectFuture;
    }

    /**
     * 只有展示的接口
     * 没有建造的接口
     */
    public String getProjectName() {
        return projectName;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public String getProjectFuture() {
        return projectFuture;
    }

    @Override
    public String toString() {
        return "code: " + getProjectCode()
                + "\nname: " + getProjectName()
                + "\nfuture: " + getProjectFuture();
    }

    /**
     * 建造者类
     */
    private static class ProjectBuilder {
        private String projectName;
        private String projectCode;
        private String projectFuture;

        /**
         * 可以在这里设置一些final的属性
         */
        private ProjectBuilder(){

        }

        public static ProjectBuilder newProjectBuilder(){
            return new ProjectBuilder();
        }

        /**
         * 在这里去构建
         */
        public ProjectBuilder addName(String projectName){
            this.projectName = projectName;
            return this;
        }

        public ProjectBuilder addCode(String projectCode){
            this.projectCode = projectCode;
            return this;
        }

        public ProjectBuilder addFuture(String projectFuture){
            this.projectFuture = projectFuture;
            return this;
        }

        /**
         * 最后是创建Project
         */
        public Project build(){
            return new Project(this);
        }
    }

    public static void main(String[] args) {
        Project.ProjectBuilder builder = ProjectBuilder.newProjectBuilder();
        System.out.println(
                builder.addCode("100").addFuture("good").addName("Marvel").build().toString()
        );
    }
}
