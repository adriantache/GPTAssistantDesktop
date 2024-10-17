package new_structure.data.persona

import new_structure.data.persona.dataSource.PersonaDataSource
import new_structure.domain.persona.data.PersonaRepository
import new_structure.domain.persona.data.model.PersonaData

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
