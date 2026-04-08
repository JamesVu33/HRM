package com.example.ihrm.domain.model

import com.example.ihrm.data.remote.organizationresponse.EmployeeDepartmentResponse
import com.example.ihrm.data.remote.organizationresponse.OrganizationResponse
import com.example.ihrm.ui.organization.OrganizationTreeNode
import kotlin.text.orEmpty

data class Department(
    val id: String,
    val code: String,
    val name: String,
    val description: String,
    val parentId: String?,
    val order: Int,
    val requireSecurityCheck: Boolean,
    val leader: DepartmentMember?,
    val members: List<DepartmentMember>,
    val children: List<String>,
    val memberCount: Int,
    val depth: Int = 0
)

data class DepartmentMember(
    val id: Int,
    val employeeId: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val titleName: String?,
    val levelName: String?,
    val roleName: String?,
    val avatarUrl: String?
)

fun OrganizationResponse.toDepartment(depth: Int = 0): Department? {
    val id = id ?: return null
    val name = name ?: return null

    return Department(
        id = id,
        code = code.orEmpty(),
        name = name,
        description = description.orEmpty(),
        parentId = parentId,
        order = order ?: 0,
        requireSecurityCheck = requireSecurityCheck ?: false,
        leader = leader?.toDepartmentMember(),
        members = members?.mapNotNull { it.toDepartmentMember() } ?: emptyList(),
        children = children ?: emptyList(),
        memberCount = memberCount ?: 0,
        depth = depth
    )
}

fun EmployeeDepartmentResponse.toDepartmentMember(): DepartmentMember? {
    val id = id ?: return null
    val fullName = fullName ?: return null

    return DepartmentMember(
        id = id,
        employeeId = employeeId.orEmpty(),
        fullName = fullName,
        email = email.orEmpty(),
        phoneNumber = phoneNumber.orEmpty(),
        titleName = title?.name,
        levelName = level?.name,
        roleName = roles?.firstOrNull()?.name,
        avatarUrl = avatarUrl
    )
}

fun List<OrganizationResponse>.toFlatDepartmentList(): List<Department> {
    val map = associateBy { it.id }
    val roots = filter { it.parentId == null }.sortedBy { it.order }

    val result = mutableListOf<Department>()
    val stack = ArrayDeque<Pair<OrganizationResponse, Int>>()

    roots.reversed().forEach { stack.addFirst(it to 0) }

    while (stack.isNotEmpty()) {
        val (curr, depth) = stack.removeFirst()
        curr.toDepartment(depth)?.let { result.add(it) }

        curr.children
            ?.mapNotNull { map[it] }
            ?.sortedBy { it.order }
            ?.reversed()
            ?.forEach { stack.addFirst(it to depth + 1) }
    }

    return result
}

fun List<Department>.toTreeNodes(): List<OrganizationTreeNode> {
    val map = associateBy { it.id }

    fun Department.toNode(): OrganizationTreeNode = OrganizationTreeNode(
        id = id,
        title = name,
        roleName = leader?.roleName.orEmpty(),
        leaderInfo = leader,
        leaderName = leader?.fullName.orEmpty(),
        memberCount = memberCount.takeIf { it > 0 },
        avatarUrl = leader?.avatarUrl,
        members = members,
        children = children
            .sortedBy { childId -> map[childId]?.order ?: 0 }
            .mapNotNull { childId ->
                map[childId]?.toNode()
            }
    )

    return filter { it.parentId == null }
        .sortedBy { it.order }
        .map { it.toNode() }
}
