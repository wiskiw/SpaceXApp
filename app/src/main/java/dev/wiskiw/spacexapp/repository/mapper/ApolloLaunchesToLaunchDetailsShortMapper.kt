package dev.wiskiw.spacexapp.repository.mapper

import dev.wiskiw.spacexapp.LaunchesQuery
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsShort
import dev.wiskiw.spacexapp.domain.model.Mission

class ApolloLaunchesToLaunchDetailsShortMapper :
    Mapper<LaunchesQuery.Launch, LaunchDetailsShort>() {

    override fun map(from: LaunchesQuery.Launch) = LaunchDetailsShort(
        id = from.id,
        rocketName = from.rocket?.name.expectValue("rocket"),
        mission = mapMission(from.mission.expectValue("mission"))
    )

    private fun mapMission(from: LaunchesQuery.Mission) = Mission(
        name = from.name.expectValue("name"),
        imageUrl = from.missionPatch
    )
}
