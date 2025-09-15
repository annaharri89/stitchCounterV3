package com.example.stitchcounterv3.data.repo

import com.example.stitchcounterv3.data.local.ProjectDao
import com.example.stitchcounterv3.data.local.ProjectEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao
) {
    fun observeProjects(): Flow<List<ProjectEntity>> = projectDao.observeAll()

    suspend fun getProject(id: Int): ProjectEntity? = projectDao.getById(id)

    suspend fun upsert(entity: ProjectEntity): Long = projectDao.upsert(entity)

    suspend fun update(entity: ProjectEntity) = projectDao.update(entity)

    suspend fun delete(entity: ProjectEntity) = projectDao.delete(entity)
}

