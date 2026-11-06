# ai-software-engineer

Autonomous junior developer agent. Java 21 + Spring Boot + Spring AI + Ollama.

## Run
docker compose up (requires Ollama on host with qwen3:8b and nomic-embed-text).

Ports: postgres 5434, backend 8083, frontend 5176.

## Sandbox Maven dependencies
El contenedor sandbox corre sin red por defecto. Se monta un cache de ~/.m2 del host en modo solo lectura en /root/.m2 para que mvn test pueda resolver dependencias ya descargadas. Documentado en docker-compose (volumen .m2).

## Architecture
Agent loop: Engineer uses tools (list/read/search/edit/create/run/git_diff/finish), Reviewer approves. Trace persisted in run_step and streamed via SSE.

