package dev.wiskiw.spacexapp.data.mapper

import dev.wiskiw.spacexapp.LaunchDetailsQuery
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsFull
import dev.wiskiw.spacexapp.domain.model.Mission
import dev.wiskiw.spacexapp.domain.model.Rocket

class ApolloLaunchDetailsToLaunchDetailsFullMapper :
    Mapper<LaunchDetailsQuery.Launch, LaunchDetailsFull>() {

    override fun map(from: LaunchDetailsQuery.Launch) = LaunchDetailsFull(
        id = from.id.expectValue("id"),
        mission = mapMission(from.mission.expectValue("mission")),
        rocket = mapRocket(from.rocket.expectValue("rocket"))
    )

    private fun mapMission(from: LaunchDetailsQuery.Mission) = Mission(
        name = from.name.expectValue("name"),
        imageUrl = from.missionPatch
    )

    private fun mapRocket(from: LaunchDetailsQuery.Rocket) = Rocket(
        id = from.id,
        name = from.name.expectValue("name"),
        type = from.name.expectValue("type")
    )
}
