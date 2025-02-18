package io.github.coolcrabs.brachyura.ide;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.coolcrabs.brachyura.dependency.JavaJarDependency;
import io.github.coolcrabs.brachyura.util.Lazy;

import java.util.Objects;
import java.util.function.Supplier;

public final class IdeProject {
    public final String name;
    public final Lazy<List<JavaJarDependency>> dependencies;
    public final List<RunConfig> runConfigs;
    public final Map<String, Path> sourcePaths;
    public final List<Path> resourcePaths;
    public final int javaVersion;

    IdeProject(String name, Supplier<List<JavaJarDependency>> dependencies, List<RunConfig> runConfigs, Map<String, Path> sourcePaths, List<Path> resourcePaths, int javaVersion) {
        this.name = name;
        this.dependencies = new Lazy<>(dependencies);
        this.runConfigs = runConfigs;
        this.sourcePaths = sourcePaths;
        this.resourcePaths = resourcePaths;
        this.javaVersion = javaVersion;
    }

    public static class IdeProjectBuilder {
        private String name = "BrachyuraProject";
        private Supplier<List<JavaJarDependency>> dependencies = Collections::emptyList;
        private List<RunConfig> runConfigs = Collections.emptyList();
        private Map<String, Path> sourcePaths = Collections.emptyMap();
        private List<Path> resourcePaths = Collections.emptyList();
        private int javaVersion = 8;
        
        public IdeProjectBuilder name(String name) {
            this.name = name;
            return this;
        }

        public IdeProjectBuilder dependencies(Supplier<List<JavaJarDependency>> dependencies) {
            this.dependencies = dependencies;
            return this;
        }
        
        public IdeProjectBuilder dependencies(List<JavaJarDependency> dependencies) {
            this.dependencies = () -> dependencies;
            return this;
        }

        public IdeProjectBuilder dependencies(JavaJarDependency... dependencies) {
            this.dependencies = () -> Arrays.asList(dependencies);
            return this;
        }

        public IdeProjectBuilder runConfigs(List<RunConfig> runConfigs) {
            this.runConfigs = runConfigs;
            return this;
        }

        public IdeProjectBuilder runConfigs(RunConfig... runConfigs) {
            this.runConfigs = Arrays.asList(runConfigs);
            return this;
        }

        public IdeProjectBuilder sourcePaths(Map<String, Path> sourcePaths) {
            this.sourcePaths = sourcePaths;
            return this;
        }

        public IdeProjectBuilder sourcePath(Path sourcePath) {
            this.sourcePaths = new HashMap<>();
            sourcePaths.put("src", sourcePath);
            return this;
        }

        public IdeProjectBuilder resourcePaths(List<Path> resourcePaths) {
            this.resourcePaths = resourcePaths;
            return this;
        }

        public IdeProjectBuilder resourcePaths(Path... resourcePaths) {
            this.resourcePaths = Arrays.asList(resourcePaths);
            return this;
        }
        
        public IdeProjectBuilder javaVersion(int javaVersion) {
            this.javaVersion = javaVersion;
            return this;
        }

        public IdeProject build() {
            return new IdeProject(name, dependencies, runConfigs, sourcePaths, resourcePaths, javaVersion);
        }
    }

    public static class RunConfig {
        public final String name;
        public final String mainClass;
        public final Path cwd; // Make sure this exists
        public final Lazy<List<String>> vmArgs;
        public final Lazy<List<String>> args;
        public final Lazy<List<Path>> classpath;
        public final List<Path> resourcePaths;

        RunConfig(String name, String mainClass, Path cwd, Supplier<List<String>> vmArgs, Supplier<List<String>> args, Supplier<List<Path>> classpath, List<Path> resourcePaths) {
            this.name = name;
            this.mainClass = mainClass;
            this.cwd = cwd;
            this.vmArgs = new Lazy<>(vmArgs);
            this.args = new Lazy<>(args);
            this.classpath = new Lazy<>(classpath);
            this.resourcePaths = resourcePaths;
        }

        public static class RunConfigBuilder {
            private String name;
            private String mainClass;
            private Path cwd;
            private Supplier<List<String>> vmArgs = Collections::emptyList;
            private Supplier<List<String>> args = Collections::emptyList;
            private Supplier<List<Path>> classpath = Collections::emptyList;
            private List<Path> resourcePaths = Collections.emptyList();

            public RunConfigBuilder name(String name) {
                this.name = name;
                return this;
            }

            public RunConfigBuilder mainClass(String mainClass) {
                this.mainClass = mainClass;
                return this;
            }

            public RunConfigBuilder cwd(Path cwd) {
                this.cwd = cwd;
                return this;
            }

            public RunConfigBuilder vmArgs(Supplier<List<String>> vmArgs) {
                this.vmArgs = vmArgs;
                return this;
            }

            public RunConfigBuilder vmArgs(List<String> vmArgs) {
                this.vmArgs = () -> vmArgs;
                return this;
            }

            public RunConfigBuilder vmArgs(String... vmArgs) {
                this.vmArgs = () -> Arrays.asList(vmArgs);
                return this;
            }

            public RunConfigBuilder args(Supplier<List<String>> args) {
                this.args = args;
                return this;
            }

            public RunConfigBuilder args(List<String> args) {
                this.args = () -> args;
                return this;
            }

            public RunConfigBuilder args(String... args) {
                this.args = () -> Arrays.asList(args);
                return this;
            }

            public RunConfigBuilder classpath(Supplier<List<Path>> classpath) {
                this.classpath = classpath;
                return this;
            }

            public RunConfigBuilder classpath(List<Path> classpath) {
                this.classpath = () -> classpath;
                return this;
            }

            public RunConfigBuilder classpath(Path... classpath) {
                this.classpath = () -> Arrays.asList(classpath);
                return this;
            }
            
            public RunConfigBuilder resourcePaths(List<Path> resourcePaths) {
                this.resourcePaths = resourcePaths;
                return this;
            }

            public RunConfigBuilder resourcePaths(Path... resourcePaths) {
                this.resourcePaths = Arrays.asList(resourcePaths);
                return this;
            }

            public RunConfig build() {
                Objects.requireNonNull(name, "Null name");
                Objects.requireNonNull(mainClass, "Null mainClass");
                Objects.requireNonNull(cwd, "Null cwd");
                return new RunConfig(name, mainClass, cwd, vmArgs, args, classpath, resourcePaths);
            }
        }
    }
}
