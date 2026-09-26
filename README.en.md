# VerifyHash

> [Català](./README.md) | **English**

Command-line tool in Java that verifies whether a given **SHA-256** hash corresponds to a text or to a file.

## Project Description

The program computes the SHA-256 hash of an input value (either a direct text or a file) and compares it against an expected hash passed as an argument. If they match, it prints a confirmation message; if not, it prints the generated hash so it can be compared manually.

Main features:

- SHA-256 hashing using `java.security.MessageDigest`.
- Constant-time comparison with `MessageDigest.isEqual` (avoids timing attacks).
- Two input modes: `text` (direct string) and `file` (file path).
- Validation of parameters, hash format and file existence.

## Project Structure

| Path | Description |
| --- | --- |
| `src/main/java/org/yourcompany/yourproject/Hash.java` | Application source code. |
| `pom.xml` | Maven project (Java 17), produces the `VerifyHash.jar` JAR. |
| `.vscode/launch.json` | Run configurations for VS Code. |
| `.vscode/tasks.json` | Build task (`mvn clean package`). |
| `target/` | Maven output (compiled classes, JAR). |

## Code Description

### Main Class

`Hash.java` contains the public class `org.yourcompany.yourproject.Hash` with a single entry point:

```java
public static void main(String[] args)
```

### Argument Parsing

`args` is looped over, handling three parameters:

- `--mode text|file` → direct text or file hash.
- `--input <data>` → text to hash or file path.
- `--hash <hash>` → expected hash in hexadecimal.

Any other parameter produces a warning error message.

### Validation

Before computing anything, the program checks (`Hash.java:34`):

- That all three mandatory parameters are present.
- That the expected hash is exactly 64 hex characters (`[0-9a-fA-F]+`).
- That the mode is either `text` or `file` (case-insensitive).

### Input Modes

- **`text` mode (`Hash.java:47`)**: computes the digest of the `String` encoded in UTF-8 and compares it with the expected hash.
- **`file` mode (`Hash.java:56`)**: checks that the file exists (if not, error with `System.exit(1)`), reads all bytes with `Files.readAllBytes` and computes their digest. Handles `IOException`.

### Private Helper Methods

| Method | Line | Purpose |
| --- | --- | --- |
| `Hexadecimal(byte[] resumen)` | `Hash.java:80` | Converts the digest into an uppercase hexadecimal string. |
| `hexToBytes(String hex)` | `Hash.java:90` | Converts a hex string into a `byte` array. |

Both methods can be said to form the core of the conversion between the binary digest format and its hexadecimal representation.

## Build and Run

Requirements: **JDK 17** (configured in `pom.xml` via `maven.compiler.release`) and **Maven**.

### With the JAR (recommended)

```powershell
mvn clean package
java -jar target\VerifyHash.jar --mode text --input "Hola mon" --hash 2ec055896d805f445338ba8432db70df69bdb044ad41b93fc674ed9ed0e0bb97
```

### From VS Code

The project already comes configured to run from the IDE:

| Configuration (`.vscode/launch.json`) | What it does |
| --- | --- |
| `Executa hash (text de prova)` | Launches the app with `--mode text`, `--input "Hola mon"` and the known hash, without asking anything. |
| `Executa hash (demanar arguments)` | Prompts for the mode, the data and the expected hash in three dialogs before launching. |

Both configurations run the `hash: package jar` task (`.vscode/tasks.json`) beforehand, that is, a `mvn clean package`.

## Examples

> The messages printed by the program are in Catalan; the output samples below are shown verbatim.

### Text

```powershell
java -jar target\VerifyHash.jar --mode text --input "Hola mon" --hash 2ec055896d805f445338ba8432db70df69bdb044ad41b93fc674ed9ed0e0bb97
```

Output:

```
Processant text directe: Hola mon
Hash coincideix!
```

With an incorrect hash, the generated one is printed:

```
Processant text directe: Hola mon
Hash no coincideix!
Hash generat: 2EC055896D805F445338BA8432DB70DF69BDB044AD41B93FC674ED9ED0E0BB97
```

### File

```powershell
java -jar target\VerifyHash.jar --mode file --input "C:\exemple\document.pdf" --hash <expected-hash>
```

If the file does not exist, it prints:

```
Llegint el fitxer a la ruta: C:\exemple\document.pdf
El fitxer no existeix: C:\exemple\document.pdf
```

## License

This project is released under **[The Unlicense](https://unlicense.org/)**: the code is in the public domain and may be copied, modified, distributed and used without conditions, for commercial or non-commercial purposes alike. The full text is in the [`LICENSE`](./LICENSE) file.

Feel free to use its code as a starting point for your own project without having to attribute it.
