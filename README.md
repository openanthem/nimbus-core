
# NIMBUS-CORE
[![license](https://img.shields.io/github/license/openanthem/nimbus-core.svg?style=for-the-badge)]() 
[![Sonatype Nexus (Milestone Build)](https://img.shields.io/maven-central/v/com.antheminc.oss/nimbus-core?label=Latest&style=for-the-badge)](https://oss.sonatype.org/#nexus-search;quick~com.antheminc.oss)
[![GitHub contributors](https://img.shields.io/github/contributors/openanthem/nimbus-core.svg?style=for-the-badge)]()
[![GitHub last commit](https://img.shields.io/github/last-commit/openanthem/nimbus-core.svg?style=for-the-badge)]()

The Nimbus Framework is a Java/Spring based platform dedicated to building highly configurable, workflow driven, and rich single page web applications through the use of configuration written atop the Java Virtual Machine. The intent of the Nimbus Framework is to provide support for software engineers in creating new applications based on current and emerging best practices by creating a flexible, expandable product that can grow and change as its customer’s needs change while minimizing the need to rewrite large sections of the code and while minimizing the overall cost of change.

View the latest documentation [here](https://openanthem.github.io/nimbus-docs/latest).

# Code Quality Metrics
<!--
## Codeclimate
[![Maintainability](https://api.codeclimate.com/v1/badges/c6e5a784163a6ce69242/maintainability)](https://codeclimate.com/github/openanthem/nimbus-core/maintainability)
-->

## Sonar
[**Master Branch**](https://sonarcloud.io/dashboard?id=openanthem%3Animbus-core-master)  
![Sonarcloud.io Security](https://sonarcloud.io/api/project_badges/measure?project=openanthem%3Animbus-core-master&metric=security_rating&style=for-the-badge)
[![Sonarcloud.io Bugs](https://sonarcloud.io/api/project_badges/measure?project=openanthem%3Animbus-core-master&metric=bugs&style=for-the-badge)]() 
[![Sonarcloud.io Coverage](https://sonarcloud.io/api/project_badges/measure?project=openanthem%3Animbus-core-master&metric=coverage&style=for-the-badge)]() [![Sonarcloud.io Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=openanthem%3Animbus-core-master&metric=sqale_index&style=for-the-badge)]()

# Development Overview
Building complex single page web applications as a Java developer has never been easier. The following "Java configuration" is all that is needed to configure a Nimbus web application.

```java
@Domain(value = "homepage", includeListeners = { ListenerType.websocket })
@Repo(value = Repo.Database.rep_none, cache = Repo.Cache.rep_device)
@ViewRoot(layout = "")
@Getter @Setter
public class VRHomepage {

    @Page(defaultPage = true)
    private VPMain vpMain;

    @Model @Getter @Setter
    public static class VPMain {

        @Tile
        private VTMain vtMain;
    }

    @Model @Getter @Setter
    public static class VTMain {

        @Section
        private VSMain vsMain;
    }

    @Model @Getter @Setter
    public static class VSMain {

        @Label("Hello World!")
        @Paragraph
        private String hello;
    }
}
```

Check out the [Nimbus Getting Started Guide](https://openanthem.github.io/nimbus-docs/latest/getting-started) for more help in getting up and running quickly. 

## Project Documentation
The latest documentation can be found [here](https://openanthem.github.io/nimbus-docs/latest). We recommend the [fundamentals section](https://openanthem.github.io/nimbus-docs/latest/fundamentals) for an in-depth guide regarding all Nimbus features.

Documentation for specific framework versions can be found [here](https://openanthem.github.io/nimbus-docs/).

## Contributing
We welcome contributions! You must review and sign our [Contributor licensing agreement](https://cla-oss.herokuapp.com/) in order to contribute. The full contribution workflow is described [here](CONTRIBUTING.md).

## Artifact Hosting
All published artifacts for Nimbus are hosted in the [Nexus OSS Repository](https://oss.sonatype.org/#nexus-search;quick~com.antheminc.oss).

## Issue Reporting
**Stuck on an implementation?**  
Please visit or the [OSS Discourse Forum](http://discourse.oss.antheminc.com/) for tips, tricks, and other helpful information related to using the Nimbus framework.

**Have an issue/feature request?**  
Please search GitHub or the [OSS Discourse Forum](http://discourse.oss.antheminc.com/) for a similar issue before submitting an issue. Create a [Github Issue](https://github.com/openanthem/nimbus-core/issues) and fill out as much detail as possible. All Github issues will be automatically created and managed within [JIRA](https://anthemopensource.atlassian.net/browse/NIMBUS) by our awesome project admin team. Issues will be reviewed on a first-come first-serve basis.

WARNING: CONTENT POSTED HERE IS PUBLIC! PLEASE REFRAIN FROM SUBMITTING ANYTHING PRODUCT SPECIFIC THAT MAY JEAPORDIZE THE INTEGRITY OF YOUR BUSINESS OR APPLICATION, INCLUDING (BUT NOT LIMITED TO): REFERENCES, COMMENTS, CODE, etc.

# Licensing
Anthem Open Source projects are licensed under *Apache License v2*. See [LICENSE](https://github.com/openanthem/oss-base/blob/master/LICENSE) for the full license text.