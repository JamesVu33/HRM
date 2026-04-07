package com.example.ihrm.ui.organization

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ihrm.R
import com.example.ihrm.domain.model.DepartmentMember
import com.example.ihrm.ui.common.BaseHRMCompose
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.domain.model.toTreeNodes
import com.example.ihrm.util.getAvatarById

// --- Design tokens (Figma node 871:41281) ---
private val OrgBlue400 = Color(0xFF0052CC)
private val OrgBlue75 = Color(0xFFB3D4FF)
private val OrgBlue50 = Color(0xFFDEEBFF)
private val OrgTitleDark = Color(0xFF101828)
private val OrgLeaderMuted = Color(0xFF4A5565)
private val OrgLeaderMuted2 = Color(0xFF6A7282)
private val OrgSearchHint = Color(0xFF99A1AF)
private val OrgDivider = Color(0xFFF3F4F6)
private val OrgExpandedRowBg = Color(0x4DEFEFFF)

private val OrgScreenGradient = Brush.linearGradient(
    colorStops = arrayOf(
        0.011469f to OrgBlue400,
        0.44737f to OrgBlue75,
        0.70454f to OrgBlue50,
        1f to Color.White
    ),
    start = Offset(0f, 0f),
    end = Offset(800f, 1400f)
)

/** Tree node for dummy data; kept in this file only (no extra types file). */
data class OrganizationTreeNode(
    val id: String,
    val title: String,
    val leaderName: String,
    val memberCount: Int? = null,
    val avatarUrl: String? = null,
    val roleName: String,
    val children: List<OrganizationTreeNode> = emptyList(),
    val members: List<DepartmentMember> = emptyList()
)

internal sealed class OrganizationVisibleRow {
    data class DepartmentRow(
        val node: OrganizationTreeNode,
        val depth: Int,
        val isExpanded: Boolean,
        val hasChildren: Boolean
    ) : OrganizationVisibleRow()

    data class MemberRow(
        val member: DepartmentMember,
        val depth: Int
    ) : OrganizationVisibleRow()
}

/**
 * Depth-first visible rows given [expandedIds] (node [OrganizationTreeNode.id] keys).
 */
internal fun visibleOrganizationRows(
    roots: List<OrganizationTreeNode>,
    expandedIds: Set<String>
): List<OrganizationVisibleRow> {
    val out = mutableListOf<OrganizationVisibleRow>()

    fun walk(nodes: List<OrganizationTreeNode>, depth: Int) {
        for (node in nodes) {
            val hasChildren = node.children.isNotEmpty() || node.members.isNotEmpty()
            val expanded = expandedIds.contains(node.id)

            out.add(OrganizationVisibleRow.DepartmentRow(node, depth, expanded, hasChildren))

            if (expanded) {
                if (node.children.isNotEmpty()) {
                    walk(node.children, depth + 1)
                }
                node.members.forEach { member ->
                    out.add(OrganizationVisibleRow.MemberRow(member, depth + 1))
                }
            }
        }
    }

    walk(roots, 0)
    return out
}

@Composable
fun OrganizationScreen(
    viewModel: OrganizationViewModel = hiltViewModel(),
    onMenuClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    BaseHRMCompose(
        content = {
            OrganizationScreenContent(
                onMenuClick = onMenuClick,
                viewModel = viewModel
            )
        },
        viewmodel = viewModel,
        onErrorAlertClose = onBackClick
    )
}

@Composable
private fun OrganizationScreenContent(
    onMenuClick: () -> Unit,
    viewModel: OrganizationViewModel,
) {
    val departments by viewModel.uiState.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }

    val rootIds = remember(departments) {
        departments.filter { it.depth == 0 }.map { it.id }.toSet()
    }
    var expandedIds by remember(rootIds) { mutableStateOf(rootIds) }

    val treeRoots = remember(departments) {
        departments.toTreeNodes()
    }

    val allRows = remember(treeRoots, expandedIds) {
        visibleOrganizationRows(treeRoots, expandedIds)
    }

    val filteredRows = remember(allRows, searchQuery) {
        val q = searchQuery.trim()
        if (q.isEmpty()) allRows
        else allRows.filter { row ->
            when (row) {
                is OrganizationVisibleRow.DepartmentRow -> {
                    row.node.title.contains(q, ignoreCase = true) ||
                            row.node.leaderName.contains(q, ignoreCase = true)
                }

                is OrganizationVisibleRow.MemberRow -> {
                    row.member.fullName.contains(q, ignoreCase = true) ||
                            row.member.email.contains(q, ignoreCase = true)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OrgScreenGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 20.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = stringResource(R.string.organization_cd_open_menu),
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = stringResource(R.string.drawer_section_organization),
                    color = Color.White,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    letterSpacing = (-0.46).sp
                )
            }

            OrganizationSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                color = Color.White,
                shadowElevation = 3.dp
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(
                        items = filteredRows,
                        key = { _, row ->
                            when (row) {
                                is OrganizationVisibleRow.DepartmentRow -> row.node.id
                                is OrganizationVisibleRow.MemberRow -> "member_${row.member.id}"
                            }
                        }
                    ) { index, row ->
                        when (row) {
                            is OrganizationVisibleRow.DepartmentRow -> {
                                if (index == 1 && filteredRows.firstOrNull()
                                        .let { it is OrganizationVisibleRow.DepartmentRow && it.depth == 0 }
                                ) {
                                    HorizontalDivider(thickness = 1.dp, color = OrgDivider)
                                }
                                OrganizationTreeRow(
                                    row = row,
                                    isRoot = row.depth == 0,
                                    onToggleExpand = {
                                        if (!row.hasChildren) return@OrganizationTreeRow
                                        expandedIds = if (row.isExpanded) {
                                            expandedIds - row.node.id
                                        } else {
                                            expandedIds + row.node.id
                                        }
                                    }
                                )
                            }

                            is OrganizationVisibleRow.MemberRow -> {
                                OrganizationMemberRow(row = row)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrganizationSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(46.dp),
        shape = RoundedCornerShape(14.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = OrgSearchHint,
                modifier = Modifier.size(18.dp)
            )
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    color = OrgTitleDark,
                    fontSize = 17.sp,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Normal
                ),
                singleLine = true,
                cursorBrush = SolidColor(OrgBlue400),
                decorationBox = { inner ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (query.isEmpty()) {
                            Text(
                                text = stringResource(R.string.organization_search_hint),
                                color = OrgSearchHint,
                                fontSize = 17.sp,
                                fontFamily = InterFontFamily
                            )
                        }
                        inner()
                    }
                }
            )
        }
    }
}

@Composable
private fun OrganizationTreeRow(
    row: OrganizationVisibleRow.DepartmentRow,
    isRoot: Boolean,
    onToggleExpand: () -> Unit
) {
    val node = row.node
    val indent = 8.dp + (20.dp * row.depth)
    val showHighlight = row.hasChildren && row.isExpanded && !isRoot

    val titleStyle = when {
        isRoot -> TextStyle(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 19.sp,
            color = OrgTitleDark,
            letterSpacing = (-0.45).sp
        )

        row.depth == 1 -> TextStyle(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = OrgTitleDark,
            letterSpacing = (-0.31).sp
        )

        else -> TextStyle(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            color = OrgTitleDark,
            letterSpacing = (-0.23).sp
        )
    }
    val leaderStyle = when {
        isRoot -> TextStyle(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = OrgLeaderMuted,
            letterSpacing = (-0.15).sp
        )

        else -> TextStyle(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = OrgLeaderMuted2,
            letterSpacing = (-0.08).sp
        )
    }

    val rowHeight = if (isRoot) 94.dp else 75.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight)
            .background(if (showHighlight) OrgExpandedRowBg else Color.Transparent)
            .padding(start = indent, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.width(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (row.hasChildren) {
                Icon(
                    imageVector = if (row.isExpanded) {
                        Icons.Default.KeyboardArrowDown
                    } else {
                        Icons.AutoMirrored.Filled.KeyboardArrowRight
                    },
                    contentDescription = stringResource(R.string.organization_cd_toggle_expand),
                    tint = OrgLeaderMuted2,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(onClick = onToggleExpand)
                )
            }
        }

        AvatarOrganization(
            avatarUrl = node.avatarUrl,
            idForRandom = node.id,
            contentDescription = node.title
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, end = 8.dp)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = node.title,
                style = titleStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = node.leaderName,
                style = leaderStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = node.roleName,
                style = leaderStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        node.memberCount?.let { count ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Groups,
                    contentDescription = stringResource(R.string.organization_cd_member_count),
                    tint = OrgLeaderMuted2,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = count.toString(),
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = if (isRoot) 13.sp else 14.sp,
                    color = OrgLeaderMuted2
                )
            }
        }
    }
}

@Composable
private fun OrganizationMemberRow(row: OrganizationVisibleRow.MemberRow) {
    val member = row.member
    val indent = 8.dp + (20.dp * row.depth)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(start = indent, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(24.dp))

        AvatarOrganization(
            avatarUrl = member.avatarUrl,
            idForRandom = member.id.toString(),
            contentDescription = member.titleName?: "Member Avatar"
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = member.fullName,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = OrgTitleDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            member.titleName?.let {
                Text(
                    text = it,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = OrgLeaderMuted2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun AvatarOrganization(
    avatarUrl: String?,
    idForRandom: String,
    contentDescription: String,
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(OrgBlue50),
        contentAlignment = Alignment.Center
    ) {
        val customPlaceholder = getAvatarById(idForRandom)

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(avatarUrl)
                .crossfade(true)
                .build(),
            placeholder = customPlaceholder,
            error = customPlaceholder,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
