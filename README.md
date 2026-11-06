# ai-software-engineer

Agente de IA que actúa como desarrollador junior autónomo. Recibe la ruta/URL de un repositorio y una tarea en lenguaje natural, explora, planea, modifica archivos en un sandbox aislado, compila y ejecuta tests, corrige errores y entrega un diff con resumen.

Stack: Java 21, Spring Boot 3.5, Spring AI 1.1 (Ollama), PostgreSQL + pgvector, Docker sandbox, JGit, React + Vite.

## Run

Requisitos: Ollama en el host con `qwen3:8b` y `nomic-embed-text`.

```
docker compose down -v
docker compose up --build
```

- Frontend: http://localhost:5176
- Backend: http://localhost:8083
- Postgres: localhost:5434

## Arquitectura

Paquetes bajo `com.abel.aisoftwareengineer`: api, agent, tools, sandbox, repo, indexing, trace, eval, config.

Roles: Engineer (usa herramientas) y Reviewer (aprueba/rechaza diff). Bucle con límites: max 25 iteraciones, timeout por tarea, detección de bucles (3 repeticiones), watchdog TIMED_OUT.

Herramientas: list_files, read_file, search_code, semantic_search_code (opcional), edit_file, create_file, run_command, git_diff, finish.

Seguridad: sandbox sin red por defecto, usuario no root, límites CPU/memoria/PIDs, path traversal bloqueado, comandos permitidos configurables, timeouts.

## Sandbox y dependencias Maven

El contenedor sandbox corre sin red. Se monta un cache de `~/.m2` del host en modo solo lectura para resolver dependencias ya descargadas. Documentado en docker-compose (volumen `.m2`).

## Trazabilidad

Cada paso se persiste en `run_step` y se emite por SSE en `/api/tasks/{id}/events`.

## Evaluación

`POST /api/eval/run` ejecuta el banco. Repo de prueba: `eval/java-calc`. Resultados reales: pendiente de ejecución completa (no implementado ejecutor automático completo).

## Tests

```
cd backend && mvn test
```

11 tests pasando (PathGuard, herramientas, allowlist). Watchdog implementado; tests del orquestador con LLM simulado no implementados.
