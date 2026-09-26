# VerifyHash

> **Català** | [English](./README.en.md)

Eina de línia de comandes en Java per verificar que un hash **SHA-256** donat correspon a un text o a un fitxer.

## Descripció del projecte

El programa calcula el hash SHA-256 d'una dada d'entrada (un text directe o un fitxer) i el compara amb un hash esperat que es passa com a argument. Si coincideixen, mostra un missatge de confirmació; si no, mostra el hash generat perquè es pugui comparar manualment.

Característiques principals:

- Hash SHA-256 utilitzant `java.security.MessageDigest`.
- Comparació segura amb `MessageDigest.isEqual` (evita atacs de temporització).
- Dos modes d'entrada: `text` (string directe) i `file` (ruta d'un fitxer).
- Validació de paràmetres, format del hash i existència del fitxer.

## Estructura del projecte

| Ruta | Descripció |
| --- | --- |
| `src/main/java/org/yourcompany/yourproject/Hash.java` | Codi font de l'aplicació. |
| `pom.xml` | Projecte Maven (Java 17), genera el JAR `VerifyHash.jar`. |
| `.vscode/launch.json` | Configuracions d'execució des de VS Code. |
| `.vscode/tasks.json` | Tasca de construcció (`mvn clean package`). |
| `target/` | Sortida de Maven (classes compilades, JAR). |

## Descripció del codi

### Classe principal

`Hash.java` conté la classe pública `org.yourcompany.yourproject.Hash` amb un únic punt d'entrada:

```java
public static void main(String[] args)
```

### Anàlisi d'arguments

Es fa un bucle sobre `args` tractant tres paràmetres:

- `--mode text|file` → text directe o hash de fitxer.
- `--input <dada>` → text a hashejar o ruta del fitxer.
- `--hash <hash>` → hash esperat en hexadecimal.

Qualsevol altre paràmetre genera un missatge d'error avís.

### Validacions

Abans de calcular res, el programa comprova (`Hash.java:34`):

- Que els tres paràmetres obligatoris estan presents.
- Que el hash esperat sigui exactament 64 caràcters hex (`[0-9a-fA-F]+`).
- Que el mode sigui `text` o `file` (no distingeix majúscules/minúscules).

### Modes d'entrada

- **Mode `text` (`Hash.java:47`)**: calcula el digest del `String` codificat en UTF-8 i el compara amb el hash esperat.
- **Mode `file` (`Hash.java:56`)**: comprova que el fitxer existeixi (si no, error amb `System.exit(1)`), llegeix tots els bytes amb `Files.readAllBytes` i en calcula el digest. Gestiona `IOException`.

### Mètodes auxiliars privats

| Mètode | Línia | Funció |
| --- | --- | --- |
| `Hexadecimal(byte[] resumen)` | `Hash.java:80` | Converteix el resum en string hexadecimal en majúscules. |
| `hexToBytes(String hex)` | `Hash.java:90` | Converteix un hex string en un array de `byte`. |

Ambdós mètodes es podria dir que formen el nucli de conversió entre el format binari del digest i la seva representació hexadecimal.

## Compilació i execució

Requisits: **JDK 17** (configurat a `pom.xml` amb `maven.compiler.release`) i **Maven**.

### Amb el JAR (recomanat)

```powershell
mvn clean package
java -jar target\VerifyHash.jar --mode text --input "Hola mon" --hash 2ec055896d805f445338ba8432db70df69bdb044ad41b93fc674ed9ed0e0bb97
```

### Des de VS Code

El projecte ja ve configurat per executar-se des de l'IDE:

| Configuració (`.vscode/launch.json`) | Què fa |
| --- | --- |
| `Executa hash (text de prova)` | Llança l'aplicació amb `--mode text`, `--input "Hola mon"` i el hash conegut, sense preguntar res. |
| `Executa hash (demanar arguments)` | Pregunta el mode, la dada i el hash esperat en tres diàlegs abans de llançar. |

Les dues configuracions executen la tasca `hash: package jar` (`.vscode/tasks.json`) abans de començar, és a dir, un `mvn clean package`.

## Exemples

> Els missatges que imprimeix el programa estan en català; els exemples de sortida es mostren tal qual.

### Text

```powershell
java -jar target\VerifyHash.jar --mode text --input "Hola mon" --hash 2ec055896d805f445338ba8432db70df69bdb044ad41b93fc674ed9ed0e0bb97
```

Sortida:

```
Processant text directe: Hola mon
Hash coincideix!
```

Amb un hash incorrecte, es mostra el generat:

```
Processant text directe: Hola mon
Hash no coincideix!
Hash generat: 2EC055896D805F445338BA8432DB70DF69BDB044AD41B93FC674ED9ED0E0BB97
```

### Fitxer

```powershell
java -jar target\VerifyHash.jar --mode file --input "C:\exemple\document.pdf" --hash <hash-esperat>
```

Si el fitxer no existeix, es mostra:

```
Llegint el fitxer a la ruta: C:\exemple\document.pdf
El fitxer no existeix: C:\exemple\document.pdf
```

## Llicència

Aquest projecte es publica sota **[The Unlicense](https://unlicense.org/)**: el codi és de domini públic i es pot copiar, modificar, distribuir i usar sense cap condició, amb o sense finalitat comercial. El text complet és al fitxer [`LICENSE`](./LICENSE).

Si volsgut, pots usar-ne el codi com a base per al teu projecte sense tenir d'atribuir-ne l'autoria.
