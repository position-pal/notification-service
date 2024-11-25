package io.github.positionpal.notification.application.groups

import io.github.positionpal.entities.GroupId
import io.github.positionpal.entities.UserId

/**
 * A repository managing groups and their members CRUD operations.
 */
interface GroupsRepository {

    /**
     * Adds the given [userId] to the group with the specified [groupId].
     * @return a [Result] indicating the success or failure of the operation.
     */
    suspend fun addMember(groupId: GroupId, userId: UserId): Result<Unit>

    /**
     * Removes the given [userId] from the group with the specified [groupId].
     * @return a [Result] indicating the success or failure of the operation.
     */
    suspend fun removeMember(groupId: GroupId, userId: UserId): Result<Unit>

    /**
     * @return a successful [Result] with all the members of the given [groupId] or a failed one in case of error.
     */
    suspend fun getMembersOf(groupId: GroupId): Result<Set<UserId>>

    /**
     * @return a successful [Result] with the groups of the given [userId] or a failed one in case of error.
     */
    suspend fun getGroupsOf(userId: UserId): Result<Set<GroupId>>
}
