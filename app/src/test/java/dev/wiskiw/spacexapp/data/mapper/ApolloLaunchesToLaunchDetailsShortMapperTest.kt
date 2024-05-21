package dev.wiskiw.spacexapp.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.wiskiw.spacexapp.LaunchesQuery
import dev.wiskiw.spacexapp.data.model.MapperException
import org.junit.Assert.assertThrows
import org.junit.Test

class ApolloLaunchesToLaunchDetailsShortMapperTest {


    @Test
    fun mapAllFieldsFromApolloLaunchesObject() {
        val mapper = ApolloLaunchesToLaunchDetailsShortMapper()
        val apolloObject = LaunchesQuery.Launch(
            id = "id",
            rocket = LaunchesQuery.Rocket(
                name = "rocket name",
            ),
            mission = LaunchesQuery.Mission(
                name = "mission name",
                missionPatch = "mission patch",
            ),
        )

        val domainObject = mapper.map(apolloObject)

        assertThat(domainObject.id).isEqualTo(apolloObject.id)
        assertThat(domainObject.rocketName).isEqualTo(apolloObject.rocket?.name)
        assertThat(domainObject.mission.name).isEqualTo(apolloObject.mission?.name)
        assertThat(domainObject.mission.imageUrl).isEqualTo(apolloObject.mission?.missionPatch)
    }

    @Test
    fun throwsMapperExceptionWhenRocketNameExpectedFieldIsMissing() {
        val mapper = ApolloLaunchesToLaunchDetailsShortMapper()
        val apolloObjectWithoutRocketName = LaunchesQuery.Launch(
            id = "id",
            rocket = LaunchesQuery.Rocket(
                name = null,
            ),
            mission = LaunchesQuery.Mission(
                name = "mission name",
                missionPatch = "mission patch",
            ),
        )

        val mapperException = assertThrows(MapperException::class.java) {
            mapper.map(apolloObjectWithoutRocketName)
        }

        assertThat(mapperException).isNotNull()
        assertThat(mapperException).isInstanceOf(MapperException::class.java)
        assertThat(mapperException).hasMessageThat().contains("rocket")
    }
}
