
# Jco Loader

Provides convenient self-contained [jco-jar](https://github.com/easonjim/jco-jar) 
classes with jco native library deployment and provisioning mechanism with JDK-only dependencies
for the following common use cases:
* Programmatically: embedded library extraction

# Why use jco-loader
* easy to set jco native path to system env
* you will fast use jco-jar to your project and not set any system env

## To load programmatically from your code:
```
// POM:
<dependency><!-- jco-jar -->
    <groupId>com.github.easonjim</groupId>
    <artifactId>com.sap.conn.jco.sapjco3</artifactId>
    <version>${sapjco3-version}</version>
</dependency>
<dependency><!-- jco-loader -->
    <groupId>com.github.easonjim</groupId>
    <artifactId>jco-loader</artifactId>
    <version>${jco-loader-version}</version>
</dependency>

// Required imports.
		import java.io.File;
		import com.sap.conn.jco.*;
		import com.github.easonjim.JcoProvisioner;

// Extract to default location: ${user.dir}/native 
		JcoProvisioner.provision();
		JCoRuntime runtime = JCoRuntimeFactory.getRuntime();

// Extract to user provided library extract location.
		final File location = new File("target/native");
		JcoProvisioner.provision(location);
		JCoRuntime runtime = JCoRuntimeFactory.getRuntime();
```

## Default extract location

Default library extract location used by
* ```JcoProvisioner.provision()```

will be selected in the following priority order:
 1. environment variable ```SAP_JCO_TARGET_FOLDER```
 2. system property ```sap.jco.target.folder```
 3. not set and use default hard coded ```${user.dir}/native```

so you can custom provide library location:
 1. environment variable ```SAP_JCO_SOURCE_FOLDER```
 2. system property ```sap.jco.source.folder```
 3. not set and use default hard coded ```/native```

## Compatible system version：
* Windows
* Linux
* Mac

## Maven central repositories version list:
* 3.0.11(provide jco native library version: 3.0.11)
