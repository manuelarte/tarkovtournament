This is the Axon Server distribution.

For information about the Axon Framework and Axon Server,
visit https://docs.axoniq.io.

Running Axon Server
-------------------

Axon Server can run in a cluster (this requires a license). Since Axon Server does not know on initialization if
it should run standalone, or as a node in a cluster, you will have to configure this.

To start the server on a specific node run the command:

    java -jar axonserver.jar

On the first node (or to run standalone) initialize the cluster using:

    java -jar axonserver-cli.jar init-cluster

On the other nodes, connect to the first node using:

    java -jar axonserver-cli.jar register-node -h <first-node-hostname>

For more information on setting up clusters and context check the reference guide at:

https://docs.axoniq.io/reference-guide/axon-server/installation/local-installation/axon-server-ee

Once Axon Server is running you can view its configuration using the Axon Dashboard at http://<axonserver>:8024.
Instead of doing this initialization through the command line, you can also use the Axon Dashboard to initialize.

Release Notes for version 2024.1.1
----------------------------------

Bug fixes and improvements:
- Revert optimization in replication from version 2023.2.4, as it could lead to a node entering fatal state
- Stop Axon Server from redirecting a client to a node that is in fatal state
- Reduce communication between the leader and follower and logging when a node is starting up
- Search page improvements
- Set correct permissions for persistent stream API calls
- Add validation of newly created index files
- Allow non-pristine clusters to connect to Console
- Fix the event store size in the context page


Release Notes for version 2024.1.0
----------------------------------

Persistent streams

Persistent streams provide the option to open an event stream from a client and let Axon Server track the progress. This
was already available as a preview version in 2024.0, but is now available by default. Persistent streams are supported
in Axon Framework 4.10 as an alternative to tracking or pooled streaming event processors.

For more information see https://library.axoniq.io/axon_framework_old_ref/events/event-processors/subscribing.html in the Axon Framework section.

Bug fixes and improvements

- Prevent stale threads when an Axon Server node closes the connection to another node
- Clean up metrics from disconnected clients
- prevent WARN log messages when a query completed message was received from an unexpected client
- Allow context with ephemeral events without a license
- Fix for listing event processors when there are more than 512 event processors

Docker image changes

The default Java version for the Docker images has changed from Java 11 to Java 17. This means that the docker images with tag "latest", "latest-nonroot", "2024.1.0", and "2024.1.0-nonroot" use Java 17. Java 11 based images are still available with the "-jdk-11" extensions in the tag name.

Dependency updates

- gRPC version updated to 1.65.1

Release Notes for version 2024.0.4
----------------------------------
Fixes and improvements:
- Fix for a problem starting up Axon Server with plugins configured
- Removed race condition causing possible delay in receiving first event on newly registered event handler
- Improve diagnostics package to contain full log information when "logging.config" property is set

Release Notes for version 2024.0.3
----------------------------------
Fixes and improvements:
- Add an option to reduce the number of global index segments Axon Server checks when the first event for a new
  aggregate is stored. This can be configured globally with the property
  "axoniq.axonserver.event.global-index-segments-check" or on a context level with the property
  "event.global-index-segments-check". The value is the number of global index segments to check, with a
  minimal value of 2.
- Fix for Control DB migration in case of plugin configuration properties with long values
- Updating a license through Axon Console now takes effect immediately
- Improved distribution of queries to different instances of the query handlers

New configuration parameter:
- axoniq.axonserver.event.global-index-segments-check=Integer.MAX_VALUE

Release Notes for version 2024.0.2
----------------------------------

Fixes and improvements:
- Updating a license through Axon Console now takes effect immediately
- Reduced memory usage for internal communication
- Reduced the number of threads used with large number of contexts
- UI improvements
  * the dialogs for adding replication groups, API tokens and users were not always cleared when opened
  * show the number of events in each context
  * improved notification when the current version is not the latest one
  * add option to set X-Frame-Options to SAMEORIGIN in the response messages

New configuration parameters:
- axoniq.axonserver.accesscontrol.same-origin=true (sets the X-Frame-Options header to SAMEORIGIN)
- axoniq.axonserver.event-store-background-thread-count=8
- axoniq.axonserver.event-store-processors-thread-count=8

Release Notes for version 2024.0.1
----------------------------------

Fixes and improvements:
- Fix the increasing number of threads on the running Axon Server nodes when one node in the cluster is down.
- Small fixes in the replication process:
    * remove delay in starting to synchronize with a node that is far behind
    * improve the performance for a follower catching up with the leader
    * prevent situations where a follower attempts to apply replication log entries that were already included in a snapshot
- Fix for authentication issue when multiple applications have the same token
- UI, copy token to clipboard fails when not running on a trusted URL
- UI, improved validations for applications, replication groups and contexts operations
- Improved handling for missing connection to Axon Console
- Support for Google Marketplace licenses
- Axon Server now performs a clean shutdown when it was started with incorrect node name or internal hostname/port

Release Notes for version 2024.0.0
----------------------------------

Database Update

Updated H2 database to store the Control DB, addressing some issues from previous H2 version (see the upgrade instructions in upgrade-instructions.txt or https://library.axoniq.io/axon_server_upgrade/upgrading_as_2024.html).


New Features and Improvements:
* Redesigned User Interface: The UI has been completely revamped with a modern look and feel for a better user experience. The changes include:
 - Simplified Overview Page: Access node information easily with filtering and scaling options.
 - Dedicated License Page: Track license expiry dates and view available features for non-enterprise users.
 - Monitoring Page: View important health information, display logs, and download diagnostic packages.
 - System Tasks: List and cancel running system tasks.
 - Search Event Store Page: Improved usability with removable columns, formatted code styles, and auto-composable queries.
 - Command and Queries Pages: Revamped for a better overview of messages in the system.
 - Long-Running Commands Component: View and cancel commands running longer than 1 second.
 - Scheduled Events Page: View and cancel scheduled events.
 - Streams Page (Experimental): Accessible for persistent streams if dev mode is enabled.
 - API Tokens (formerly Applications): Renamed for clarity, with improved token management.
 - Support for Wide Screens and Dark/Light Themes: Enhanced viewing experience.
 - Connection, Health, and Early Event Processor Issue Detection: Improved issue detection and resolution.
 - Embedded Documentation Snippets: Access documentation directly within the UI.
* Preview of new persistent streams feature, event streams where Axon Server manages the publication of events to clients and keeps track of the progress. This feature is enabled when development mode is enabled or when axoniq.axonserver.preview.persistent-streams property is set to true.


For release notes on earlier releases (Standard Edition and Enterprise Edition) check the release notes pages in the reference guide (https://docs.axoniq.io/reference-guide/release-notes/rn-axon-server).

Configuring Axon Server
=======================

Axon Server uses sensible defaults for all of its settings, so it will actually
run fine without any further configuration. However, if you want to make some
changes, below are the most common options. You can change them using an
"axonserver.properties" file in the directory where you run Axon Server. For the
full list, see the Reference Guide. https://docs.axoniq.io/reference-guide/axon-server

* axoniq.axonserver.name
  This is the name Axon Server uses for itself. The default is to use the
  hostname.
* axoniq.axonserver.hostname
  This is the hostname clients will use to connect to the server. Note that
  an IP address can be used if the name cannot be resolved through DNS.
  The default value is the actual hostname reported by the OS.
* server.port
  This is the port where Axon Server will listen for HTTP requests,
  by default 8024.
* axoniq.axonserver.port
  This is the port where Axon Server will listen for gRPC requests,
  by default 8124.
* axoniq.axonserver.internal-port
  This is the port where Axon Server will listen for gRPC requests from other AxonServer nodes,
  by default 8224.
* axoniq.axonserver.event.storage
  This setting determines where event messages are stored, so make sure there
  is enough diskspace here. Losing this data means losing your Events-sourced
  Aggregates' state! Conversely, if you want a quick way to start from scratch,
  here's where to clean.
* axoniq.axonserver.snapshot.storage
  This setting determines where aggregate snapshots are stored.
* axoniq.axonserver.controldb-path
  This setting determines where Axon Server stores its configuration information.
  Losing this data will affect Axon Server's ability to determine which
  applications are connected, and what types of messages they are interested
  in.
* axoniq.axonserver.replication.log-storage-folder
  This setting determines where the replication logfiles are stored.
* axoniq.axonserver.accesscontrol.enabled
  Setting this to true will require clients to pass a token.

The Axon Server HTTP server
===========================

Axon Server provides two servers; one serving HTTP requests, the other gRPC.
By default, these use ports 8024 and 8124 respectively, but you can change
these in the settings as described above.

The HTTP server has in its root context a management Web GUI, a health
indicator is available at "/actuator/health", and the REST API at "/v1'. The
API's Swagger endpoint finally, is available at "/swagger-ui/index.html", and gives
the documentation on the REST API.
