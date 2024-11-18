package data.persona

import data.persona.dataSource.PersonaDataSource
import domain.persona.data.PersonaRepository
import domain.persona.data.model.PersonaData

class PersonaRepositoryImpl(
    private val personaDataSource: PersonaDataSource = PersonaDataSource(),
) : PersonaRepository {
    override suspend fun getPersonas(): Map<String, PersonaData> {
        return personaDataSource.getPersonas()
    }

    override suspend fun addPersona(persona: PersonaData) {
        personaDataSource.addPersona(persona)
    }

    override suspend fun deletePersona(id: String) {
        personaDataSource.deletePersona(id)
    }
}
