package com.example.stitchcounterv3.domain.usecase

import com.example.stitchcounterv3.data.repo.ProjectRepository
import com.example.stitchcounterv3.domain.mapper.toDomain
import com.example.stitchcounterv3.domain.mapper.toEntity
import com.example.stitchcounterv3.domain.model.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveProjects @Inject constructor(
    private val repo: ProjectRepository
) {
    operator fun invoke(): Flow<List<Project>> = repo.observeProjects().map { list -> list.map { it.toDomain() } }
}

@Singleton
class GetProject @Inject constructor(
    private val repo: ProjectRepository
) {
    suspend operator fun invoke(id: Int): Project? = repo.getProject(id)?.toDomain()
}

@Singleton
class UpsertProject @Inject constructor(
    private val repo: ProjectRepository
) {
    suspend operator fun invoke(project: Project): Long = repo.upsert(project.toEntity())
}

@Singleton
class DeleteProject @Inject constructor(
    private val repo: ProjectRepository
) {
    suspend operator fun invoke(project: Project) = repo.delete(project.toEntity())
}

@Singleton
class DeleteProjects @Inject constructor(
    private val repo: ProjectRepository
) {
    suspend operator fun invoke(projects: List<Project>) {
        if (projects.isNotEmpty()) {
            repo.deleteByIds(projects.map { it.id })
        }
    }
}

