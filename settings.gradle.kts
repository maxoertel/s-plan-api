pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "s-plan-api"

include(":app")

include(":application-security")

include(":delivery-notes-application")
include(":delivery-notes-domain")
include(":delivery-notes-infrastructure")
include(":delivery-notes-web")

include(":domain-identity")

include(":infra-db")
include(":infra-security")
include(":infra-web")

include(":location-application")
include(":location-domain")
include(":location-infrastructure")
include(":location-web")

include(":mission-application")
include(":mission-domain")
include(":mission-infrastructure")
include(":mission-web")

include(":shared-core")

include(":user-management-application")
include(":user-management-domain")
include(":user-management-infrastructure")
include(":user-management-web")

include(":vehicle-application")
include(":vehicle-domain")
include(":vehicle-infrastructure")
include(":vehicle-web")
