# 🛒 Stateful Abandoned Cart Recovery System

An enterprise-grade, event-driven orchestration system built to recover abandoned e-commerce checkouts. This project integrates Shopify Webhooks with a custom Java 21/Spring Boot backend, orchestrated via n8n, and utilizes Google Gemini AI to dynamically generate personalized discount emails based on real-time database states.

## 🚀 Key Features

* **Event-Driven Architecture:** Listens to real-time Shopify webhook payloads (`checkouts/update`).
* **Stateful Flow Control:** Uses MySQL to track the exact state of a cart (`PENDING`, `ABANDONED`, `COMPLETED`) to prevent duplicate follow-ups.
* **AI-Powered Personalization:** Integrates Google Gemini AI via n8n to dynamically generate highly contextual, human-like recovery emails without generic placeholders.
* **Automated Escalation:** Implements delayed follow-up loops (e.g., 24-hour wait node) before querying the backend for status updates.
* **API First:** RESTful endpoints built with Spring Boot to handle webhook ingest, status checks, and attempt logging.

## 🛠️ Tech Stack

* **Backend Engine:** Java 21, Spring Boot 3.x, Spring Data JPA, Hibernate
* **Database:** MySQL 8.0
* **Orchestration & Workflow:** n8n (Node-based automation)
* **External APIs:** Shopify Webhooks, Google Gemini AI, Gmail API
* **Local Development:** Ngrok (for webhook tunneling), Postman

## 🏗️ System Architecture

```mermaid
graph TD
    classDef external fill:#f9f9f9,stroke:#333,stroke-width:2px;
    classDef n8n fill:#ff6d5a,stroke:#fff,stroke-width:2px,color:#fff;
    classDef spring fill:#6db33f,stroke:#fff,stroke-width:2px,color:#fff;
    classDef db fill:#00758f,stroke:#fff,stroke-width:2px,color:#fff;
    classDef ai fill:#4285f4,stroke:#fff,stroke-width:2px,color:#fff;

    Shopify[🛒 Shopify Store]:::external
    N8N_Webhook[⚡ n8n Webhook]:::n8n
    SpringAPI[🍃 Spring Boot REST API]:::spring
    MySQL[(🐬 MySQL Database)]:::db
    N8N_Wait[⏳ n8n Wait Node 24h]:::n8n
    N8N_Check[🔀 n8n IF/Router Node]:::n8n
    Gemini[🤖 Google Gemini AI]:::ai
    Gmail[📧 Gmail API]:::external

    Shopify -->|1. Cart Abandoned Payload| N8N_Webhook
    N8N_Webhook -->|2. POST /attempt 1| SpringAPI
    SpringAPI <-->|Save/Fetch Data| MySQL
    
    N8N_Webhook -->|3. Trigger Wait| N8N_Wait
    N8N_Wait -->|4. Time Elapsed| N8N_Check
    
    N8N_Check -->|5. GET /status| SpringAPI
    
    N8N_Check -->|6. If status == ABANDONED| Gemini
    Gemini -->|7. Generate Discount Email| N8N_Check
    
    N8N_Check -->|8. Send Email| Gmail
    N8N_Check -->|9. POST /attempt 2| SpringAPI