package new_structure.domain.persona.data

import new_structure.domain.persona.data.model.PersonaData

interface PersonaRepository {
    suspend fun getPersonas(): Map<String, PersonaData>

    suspend fun addPersona(persona: PersonaData)

    suspend fun deletePersona(id: String)
}
