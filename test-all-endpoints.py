#!/usr/bin/env python3
"""
Script completo para testar todos os endpoints da API MonitoriaWeb
Atualizado com os DTOs corretos
"""

import requests
import json
from datetime import datetime, date
from typing import Dict, Optional

BASE_URL = "http://localhost:8080/api"

class Colors:
    GREEN = '\033[92m'
    RED = '\033[91m'
    YELLOW = '\033[93m'
    BLUE = '\033[94m'
    RESET = '\033[0m'
    BOLD = '\033[1m'

class APITester:
    def __init__(self):
        self.admin_token = None
        self.professor_token = None
        self.results = {
            "total": 0,
            "passed": 0,
            "failed": 0,
            "tests": []
        }
        self.existing_data = {
            "escola_id": 1,  # ID da escola existente
            "professor_id": 1  # ID do professor existente
        }
        self.created_ids = {
            "escola": None,
            "professor": None,
            "disciplina": None,
            "aluno": None,
            "monitoria": None,
            "monitor": None,
            "assunto": None,
            "frequencia": None
        }

    def print_header(self, text: str):
        print(f"\n{Colors.BOLD}{Colors.BLUE}{'='*80}{Colors.RESET}")
        print(f"{Colors.BOLD}{Colors.BLUE}{text.center(80)}{Colors.RESET}")
        print(f"{Colors.BOLD}{Colors.BLUE}{'='*80}{Colors.RESET}\n")

    def print_test(self, method: str, endpoint: str, status: int, success: bool, message: str = ""):
        self.results["total"] += 1
        if success:
            self.results["passed"] += 1
            color = Colors.GREEN
            icon = "✓"
        else:
            self.results["failed"] += 1
            color = Colors.RED
            icon = "✗"

        self.results["tests"].append({
            "method": method,
            "endpoint": endpoint,
            "status": status,
            "success": success,
            "message": message
        })

        endpoint_display = f"{endpoint:50}"[:50]
        print(f"{color}{icon} [{method:6}] {endpoint_display} - Status: {status} {message}{Colors.RESET}")

    def test_endpoint(self, method: str, endpoint: str, data: Optional[Dict] = None,
                     token: Optional[str] = None, expected_status: int = 200,
                     description: str = "", params: Optional[Dict] = None) -> Optional[Dict]:
        """Testa um endpoint e retorna a resposta"""
        url = f"{BASE_URL}{endpoint}"
        headers = {"Content-Type": "application/json"}

        if token:
            headers["Authorization"] = f"Bearer {token}"

        try:
            if method == "GET":
                response = requests.get(url, headers=headers, params=params)
            elif method == "POST":
                response = requests.post(url, json=data, headers=headers)
            elif method == "PUT":
                response = requests.put(url, json=data, headers=headers)
            elif method == "PATCH":
                response = requests.patch(url, json=data, headers=headers, params=params)
            elif method == "DELETE":
                response = requests.delete(url, headers=headers)
            else:
                raise ValueError(f"Método HTTP inválido: {method}")

            success = response.status_code == expected_status
            message = description if description else ""

            if not success and response.status_code >= 400:
                try:
                    error_data = response.json()
                    if 'message' in error_data:
                        message = f"- {error_data['message']}"
                    elif 'validationErrors' in error_data:
                        errors = error_data['validationErrors']
                        first_error = list(errors.values())[0] if errors else 'Erro de validação'
                        message = f"- {first_error}"
                except:
                    message = f"- {response.text[:50]}"

            self.print_test(method, endpoint, response.status_code, success, message)

            if success and response.text:
                try:
                    return response.json()
                except:
                    return None
            return None

        except requests.exceptions.ConnectionError:
            self.print_test(method, endpoint, 0, False, "- Erro de conexão - Aplicação não está rodando")
            return None
        except Exception as e:
            self.print_test(method, endpoint, 0, False, f"- Erro: {str(e)}")
            return None

    def test_authentication(self):
        """Testa endpoints de autenticação"""
        self.print_header("1. AUTENTICAÇÃO (1 endpoint)")

        # Login admin
        response = self.test_endpoint(
            "POST", "/auth/login",
            {"username": "admin", "password": "admin123"},
            expected_status=200,
            description="Login Admin"
        )

        if response and "token" in response:
            self.admin_token = response["token"]
            print(f"{Colors.GREEN}✓ Token admin obtido com sucesso{Colors.RESET}")

        # Login professor (usuário existente)
        response = self.test_endpoint(
            "POST", "/auth/login",
            {"username": "joao.silva", "password": "senha123"},
            expected_status=200,
            description="Login Professor"
        )

        if response and "token" in response:
            self.professor_token = response["token"]
            print(f"{Colors.GREEN}✓ Token professor obtido com sucesso{Colors.RESET}")

    def test_admin_escolas(self):
        """Testa endpoints de Admin - Escolas"""
        if not self.admin_token:
            print(f"{Colors.YELLOW}Pulando testes de escolas - token admin não disponível{Colors.RESET}")
            return

        self.print_header("2. ADMIN - ESCOLAS (7 endpoints)")

        # 1. Criar escola
        escola_data = {
            "nome": "Escola de Teste API - " + datetime.now().strftime("%H%M%S"),
            "tipo": "EDUCACAO_CULTURA_HUMANIDADES"
        }
        response = self.test_endpoint(
            "POST", "/admin/escolas", escola_data, self.admin_token, 201
        )
        if response and "id" in response:
            self.created_ids["escola"] = response["id"]

        # 2. Listar todas
        self.test_endpoint("GET", "/admin/escolas", token=self.admin_token)

        # 3. Listar ativas
        self.test_endpoint("GET", "/admin/escolas/ativas", token=self.admin_token)

        if self.created_ids["escola"]:
            escola_id = self.created_ids["escola"]

            # 4. Buscar por ID
            self.test_endpoint("GET", f"/admin/escolas/{escola_id}", token=self.admin_token)

            # 5. Atualizar
            escola_data["nome"] = "Escola Atualizada API - " + datetime.now().strftime("%H%M%S")
            self.test_endpoint("PUT", f"/admin/escolas/{escola_id}", escola_data, self.admin_token)

            # 6. Inativar
            self.test_endpoint("PATCH", f"/admin/escolas/{escola_id}/inativar", token=self.admin_token)

            # 7. Ativar
            self.test_endpoint("PATCH", f"/admin/escolas/{escola_id}/ativar", token=self.admin_token)

    def test_admin_professores(self):
        """Testa endpoints de Admin - Professores"""
        if not self.admin_token:
            print(f"{Colors.YELLOW}Pulando testes de professores - token admin não disponível{Colors.RESET}")
            return

        self.print_header("3. ADMIN - PROFESSORES (6 endpoints)")

        # 1. Criar professor
        timestamp = datetime.now().strftime("%H%M%S")
        professor_data = {
            "numeroRegistro": f"PROF{timestamp}",
            "nomeCompleto": "Professor Teste API",
            "formacao": "MESTRADO",
            "nomeInstituicao": "UCSAL",
            "nomeCurso": "Sistemas de Informação",
            "anoConclusao": 2020,
            "escolaId": self.existing_data["escola_id"],
            "username": f"prof.teste.{timestamp}",
            "email": f"prof.teste.{timestamp}@ucsal.br",
            "password": "senha123"
        }
        response = self.test_endpoint(
            "POST", "/admin/professores", professor_data, self.admin_token, 201
        )
        if response and "id" in response:
            self.created_ids["professor"] = response["id"]

        # 2. Listar todos
        self.test_endpoint("GET", "/admin/professores", token=self.admin_token)

        # 3. Listar ativos
        self.test_endpoint("GET", "/admin/professores/ativos", token=self.admin_token)

        if self.created_ids["professor"]:
            prof_id = self.created_ids["professor"]

            # 4. Buscar por ID
            self.test_endpoint("GET", f"/admin/professores/{prof_id}", token=self.admin_token)

            # 5. Inativar
            self.test_endpoint("PATCH", f"/admin/professores/{prof_id}/inativar", token=self.admin_token)

            # 6. Ativar
            self.test_endpoint("PATCH", f"/admin/professores/{prof_id}/ativar", token=self.admin_token)

    def test_admin_disciplinas(self):
        """Testa endpoints de Admin - Disciplinas"""
        if not self.admin_token:
            print(f"{Colors.YELLOW}Pulando testes de disciplinas - token admin não disponível{Colors.RESET}")
            return

        self.print_header("4. ADMIN - DISCIPLINAS (5 endpoints)")

        # 1. Criar disciplina
        timestamp = datetime.now().strftime("%H%M%S")
        disciplina_data = {
            "sigla": f"TEST{timestamp[-4:]}",
            "nome": "Disciplina Teste API",
            "descricao": "Disciplina de teste criada via API",
            "cargaHoraria": "60h",
            "curso": "Sistemas de Informação",
            "matrizVinculada": "2024.1",
            "escolaId": self.existing_data["escola_id"],
            "professorId": self.existing_data["professor_id"]
        }
        response = self.test_endpoint(
            "POST", "/admin/disciplinas", disciplina_data, self.admin_token, 201
        )
        if response and "id" in response:
            self.created_ids["disciplina"] = response["id"]

        # 2. Listar todas
        self.test_endpoint("GET", "/admin/disciplinas", token=self.admin_token)

        if self.created_ids["disciplina"]:
            disc_id = self.created_ids["disciplina"]

            # 3. Buscar por ID
            self.test_endpoint("GET", f"/admin/disciplinas/{disc_id}", token=self.admin_token)

            # 4. Atualizar
            disciplina_data["nome"] = "Disciplina Atualizada API"
            self.test_endpoint("PUT", f"/admin/disciplinas/{disc_id}", disciplina_data, self.admin_token)

            # 5. Inativar
            self.test_endpoint("PATCH", f"/admin/disciplinas/{disc_id}/inativar", token=self.admin_token)

    def test_professor_alunos(self):
        """Testa endpoints de Professor - Alunos"""
        if not self.professor_token:
            print(f"{Colors.YELLOW}Pulando testes de alunos - token professor não disponível{Colors.RESET}")
            return

        self.print_header("5. PROFESSOR - ALUNOS (6 endpoints)")

        # 1. Criar aluno
        if self.created_ids["disciplina"]:
            timestamp = datetime.now().strftime("%H%M%S")
            aluno_data = {
                "matricula": f"2024{timestamp[-4:]}",
                "nomeCompleto": "Aluno Teste API",
                "disciplinaId": self.created_ids["disciplina"],
                "semestre": 1
            }
            response = self.test_endpoint(
                "POST", "/professor/alunos", aluno_data, self.professor_token, 201
            )
            if response and "id" in response:
                self.created_ids["aluno"] = response["id"]

        # 2. Listar meus alunos
        self.test_endpoint("GET", "/professor/alunos", token=self.professor_token)

        # 3. Listar por disciplina
        if self.created_ids["disciplina"]:
            self.test_endpoint(
                "GET", f"/professor/alunos/disciplina/{self.created_ids['disciplina']}",
                token=self.professor_token
            )

        if self.created_ids["aluno"]:
            aluno_id = self.created_ids["aluno"]

            # 4. Buscar por ID
            self.test_endpoint("GET", f"/professor/alunos/{aluno_id}", token=self.professor_token)

            # 5. Atualizar
            timestamp = datetime.now().strftime("%H%M%S")
            aluno_update = {
                "matricula": f"2024{timestamp[-4:]}",
                "nomeCompleto": "Aluno Atualizado API",
                "disciplinaId": self.created_ids["disciplina"],
                "semestre": 2
            }
            self.test_endpoint("PUT", f"/professor/alunos/{aluno_id}", aluno_update, self.professor_token)

            # 6. Inativar
            self.test_endpoint("PATCH", f"/professor/alunos/{aluno_id}/inativar", token=self.professor_token)

    def test_professor_monitorias(self):
        """Testa endpoints de Professor - Monitorias"""
        if not self.professor_token:
            print(f"{Colors.YELLOW}Pulando testes de monitorias - token professor não disponível{Colors.RESET}")
            return

        self.print_header("6. PROFESSOR - MONITORIAS (8 endpoints)")

        # 1. Criar monitoria
        if self.created_ids["disciplina"]:
            monitoria_data = {
                "disciplinaId": self.created_ids["disciplina"],
                "tipo": "PRESENCIAL",
                "local": "Sala 101",
                "dataInicio": "2024-01-15",
                "dataEncerramento": "2024-06-30",
                "horaInicio": "14:00:00",
                "horaEncerramento": "16:00:00",
                "curso": "Sistemas de Informação",
                "semestre": 1
            }
            response = self.test_endpoint(
                "POST", "/professor/monitorias", monitoria_data, self.professor_token, 201
            )
            if response and "id" in response:
                self.created_ids["monitoria"] = response["id"]

        # 2. Listar minhas monitorias
        self.test_endpoint("GET", "/professor/monitorias", token=self.professor_token)

        # 3. Listar em andamento
        self.test_endpoint("GET", "/professor/monitorias/em-andamento", token=self.professor_token)

        if self.created_ids["monitoria"]:
            mon_id = self.created_ids["monitoria"]

            # 4. Buscar por ID
            self.test_endpoint("GET", f"/professor/monitorias/{mon_id}", token=self.professor_token)

            # 5. Atualizar
            monitoria_update = {
                "disciplinaId": self.created_ids["disciplina"],
                "tipo": "PRESENCIAL",
                "local": "Sala 102 - Atualizada",
                "dataInicio": "2024-01-15",
                "dataEncerramento": "2024-12-31",
                "horaInicio": "15:00:00",
                "horaEncerramento": "17:00:00",
                "curso": "Sistemas de Informação",
                "semestre": 1
            }
            self.test_endpoint("PUT", f"/professor/monitorias/{mon_id}", monitoria_update, self.professor_token)

            # 6. Contar alunos
            self.test_endpoint("GET", f"/professor/monitorias/{mon_id}/quantidade-alunos", token=self.professor_token)

            # 7. Associar aluno (deixar para testar depois dos assuntos e frequências)
            if self.created_ids["aluno"]:
                monitor_data = {
                    "monitoriaId": mon_id,
                    "alunoId": self.created_ids["aluno"]
                }
                response = self.test_endpoint(
                    "POST", "/professor/monitorias/associar-aluno",
                    monitor_data, self.professor_token
                )
                # Salvar o ID do monitor se retornado
                if response and "id" in response:
                    self.created_ids["monitor"] = response["id"]

            # 8. Finalizar monitoria (MOVIDO PARA O FINAL - depois de testar assuntos)
            # Será testado após os assuntos e frequências

    def test_professor_assuntos(self):
        """Testa endpoints de Professor - Assuntos"""
        if not self.professor_token:
            print(f"{Colors.YELLOW}Pulando testes de assuntos - token professor não disponível{Colors.RESET}")
            return

        self.print_header("7. PROFESSOR - ASSUNTOS (3 endpoints)")

        # 1. Registrar assunto
        if self.created_ids["monitoria"]:
            assunto_data = {
                "monitoriaId": self.created_ids["monitoria"],
                "assunto": "Introdução a Testes de API",
                "data": date.today().isoformat(),
                "atividadePraticaAplicada": "Exercícios práticos com Python"
            }
            response = self.test_endpoint(
                "POST", "/professor/assuntos", assunto_data, self.professor_token, 201
            )
            if response and "id" in response:
                self.created_ids["assunto"] = response["id"]

            # 2. Listar por monitoria
            self.test_endpoint(
                "GET", f"/professor/assuntos/monitoria/{self.created_ids['monitoria']}",
                token=self.professor_token
            )

        # 3. Excluir assunto
        if self.created_ids["assunto"]:
            self.test_endpoint(
                "DELETE", f"/professor/assuntos/{self.created_ids['assunto']}",
                token=self.professor_token
            )

    def test_professor_frequencias(self):
        """Testa endpoints de Professor - Frequências"""
        if not self.professor_token:
            print(f"{Colors.YELLOW}Pulando testes de frequências - token professor não disponível{Colors.RESET}")
            return

        self.print_header("8. PROFESSOR - FREQUÊNCIAS (2 endpoints)")

        # 1. Registrar frequência (DTO correto: alunoId e monitoriaId, NÃO monitorId)
        if self.created_ids["monitoria"] and self.created_ids["aluno"]:
            frequencia_data = {
                "alunoId": self.created_ids["aluno"],
                "monitoriaId": self.created_ids["monitoria"],
                "data": date.today().isoformat(),
                "presente": True,
                "observacao": "Aluno presente e participativo"
            }
            response = self.test_endpoint(
                "POST", "/professor/frequencias", frequencia_data, self.professor_token
            )
            if response and "id" in response:
                self.created_ids["frequencia"] = response["id"]

        # 2. Atualizar frequência
        if self.created_ids["frequencia"]:
            self.test_endpoint(
                "PATCH", f"/professor/frequencias/{self.created_ids['frequencia']}",
                token=self.professor_token,
                params={"presente": False}
            )
        else:
            # Tentar buscar ID de frequência
            print(f"{Colors.YELLOW}Buscando ID de frequência...{Colors.RESET}")
            for freq_id in range(1, 20):
                test_resp = self.test_endpoint(
                    "PATCH", f"/professor/frequencias/{freq_id}",
                    token=self.professor_token,
                    params={"presente": False}
                )
                if test_resp:
                    break

    def test_finalizar_monitoria(self):
        """Testa finalizar monitoria - deve ser executado por último"""
        if not self.professor_token:
            return

        self.print_header("9. FINALIZAR MONITORIA (1 endpoint)")

        if self.created_ids["monitoria"]:
            # Finalizar monitoria
            self.test_endpoint(
                "PATCH", f"/professor/monitorias/{self.created_ids['monitoria']}/finalizar",
                token=self.professor_token,
                description="Finalizar monitoria (testado por último)"
            )

    def print_summary(self):
        """Imprime resumo dos testes"""
        self.print_header("RESUMO DOS TESTES")

        total = self.results["total"]
        passed = self.results["passed"]
        failed = self.results["failed"]
        success_rate = (passed / total * 100) if total > 0 else 0

        print(f"Total de testes executados: {total}")
        print(f"{Colors.GREEN}Sucessos: {passed}{Colors.RESET}")
        print(f"{Colors.RED}Falhas: {failed}{Colors.RESET}")
        print(f"Taxa de sucesso: {success_rate:.1f}%\n")

        if failed > 0:
            print(f"\n{Colors.BOLD}{Colors.YELLOW}Detalhes dos testes que falharam:{Colors.RESET}\n")
            for test in self.results["tests"]:
                if not test["success"]:
                    print(f"  {Colors.RED}✗ [{test['method']:6}] {test['endpoint']}{Colors.RESET}")
                    print(f"    Status: {test['status']} {test['message']}")

        print(f"\n{Colors.BOLD}IDs criados durante os testes:{Colors.RESET}")
        for key, value in self.created_ids.items():
            if value:
                print(f"  - {key}: {value}")

    def run_all_tests(self):
        """Executa todos os testes"""
        print(f"{Colors.BOLD}{Colors.BLUE}")
        print("╔════════════════════════════════════════════════════════════════════════════╗")
        print("║          TESTE AUTOMATIZADO COMPLETO - MonitoriaWeb API v2.0              ║")
        print("╚════════════════════════════════════════════════════════════════════════════╝")
        print(f"{Colors.RESET}")

        print(f"\n{Colors.BOLD}Total de endpoints a testar: 38{Colors.RESET}")
        print(f"Base URL: {BASE_URL}\n")

        self.test_authentication()
        self.test_admin_escolas()
        self.test_admin_professores()
        self.test_admin_disciplinas()
        self.test_professor_alunos()
        self.test_professor_monitorias()
        self.test_professor_assuntos()
        self.test_professor_frequencias()
        self.test_finalizar_monitoria()

        self.print_summary()

if __name__ == "__main__":
    tester = APITester()
    tester.run_all_tests()
