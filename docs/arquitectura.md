# Diagrama de despliegue

El siguiente diagrama representa el despliegue lógico del componente Donadores y Entidades dentro de la solución general, mostrando únicamente las conexiones relevantes para este componente.

```mermaid
flowchart LR

Cliente --> APIGateway["API Gateway"]

subgraph NuestraSolucion["Nuestra Solución"]

    DonadoresEntidades["Servicio de Donadores y Entidades"]

    Donaciones["Servicio de Donaciones"]
    Incentivos["Servicio de Incentivos"]
    Logistica["Servicio de Logística"]

    Fachada["Fachada"]

    Donaciones -->|"Verifica existencia de donador"| DonadoresEntidades
    Donaciones -->|"Consulta si puede donar"| DonadoresEntidades
    Donaciones -->|"Ingresa queja"| DonadoresEntidades

    DonadoresEntidades -->|"Consulta insignias"| Incentivos
    DonadoresEntidades -->|"Consulta misión en curso"| Incentivos

    Incentivos -->|"Verifica existencia de donador"| DonadoresEntidades

    Logistica -->|"Consulta necesidades insatisfechas"| DonadoresEntidades
    Logistica -->|"Satisface necesidad material"| DonadoresEntidades

    DonadoresEntidades --> Fachada

end

APIGateway --> DonadoresEntidades
```