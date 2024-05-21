package dev.wiskiw.spacexapp.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.wiskiw.spacexapp.LaunchDetailsQuery
import dev.wiskiw.spacexapp.data.model.MapperException
import org.junit.Assert
import org.junit.Test

class ApolloLaunchDetailsToLaunchDetailsFullMapperTest {


    @Test
    fun mapAllFieldsFromApolloLaunchesObject() {
        val mapper = ApolloLaunchDetailsToLaunchDetailsFullMapper()
        val apolloObject = LaunchDetailsQuery.Launch(
            id = "id",
            rocket = LaunchDetailsQuery.Rocket(
                id = "rocket id",
                name = "rocket name",
                type = "rocket type",
            ),
            mission = LaunchDetailsQuery.Mission(
                name = "mission name",
                missionPatch = "mission patch",
            ),
            site = "site",
        )

        val domainObject = mapper.map(apolloObject)

        assertThat(domainObject.id).isEqualTo(apolloObject.id)
        assertThat(domainObject.rocket.name).isEqualTo(apolloObject.rocket?.name)
        assertThat(domainObject.rocket.id).isEqualTo(apolloObject.rocket?.id)
        assertThat(domainObject.rocket.type).isEqualTo(apolloObject.rocket?.type)
        assertThat(domainObject.mission.name).isEqualTo(apolloObject.mission?.name)
        assertThat(domainObject.mission.imageUrl).isEqualTo(apolloObject.mission?.missionPatch)
        assertThat(domainObject.site).isEqualTo(apolloObject.site)
    }

    @Test
    fun throwsMapperExceptionWhenRocketNameExpectedFieldIsMissing() {
        val mapper = ApolloLaunchDetailsToLaunchDetailsFullMapper()
        val apolloObjectWithoutRocketName = LaunchDetailsQuery.Launch(
            id = "id",
            rocket = LaunchDetailsQuery.Rocket(
                id = "rocket id",
                name = null,
                type = "rocket type",
            ),
            mission = LaunchDetailsQuery.Mission(
                name = "mission name",
                missionPatch = "mission patch",
            ),
            site = "site",
        )

        val mapperException = Assert.assertThrows(MapperException::class.java) {
            mapper.map(apolloObjectWithoutRocketName)
        }

        assertThat(mapperException).isNotNull()
        assertThat(mapperException).isInstanceOf(MapperException::class.java)
        assertThat(mapperException).hasMessageThat().contains("name")
    }
}
