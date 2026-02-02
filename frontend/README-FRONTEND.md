# Frontend – Workforce Management (Vue + Vite)

## Грешка: "Failed to load url /src/main.js" или "resolved id: ... Employee Shift & Workforce ..."

**Причина:** Името на папката на проекта съдържа символа **`&`** (`Employee Shift & Workforce Management System`). Node.js/Vite на Windows объркват този път и не могат да заредят файловете.

**Решение:** Преименувай папката на проекта така, че да **няма `&` и по възможност интервали**.

### Стъпки

1. Затвори Cursor (или поне затвори този проект) и всички прозорци/терминали, отворени в тази папка.
2. В **Проводника** отиди в `C:\Users\jnisl\IdeaProjects\`.
3. Преименувай папката:
   - от: `Employee Shift & Workforce Management System`
   - на: например **`WorkforceManagement`** или **`EmployeeShiftManagement`**
4. Отвори отново проекта в Cursor от новата папка (напр. `C:\Users\jnisl\IdeaProjects\WorkforceManagement`).
5. Стартирай frontend-а:
   ```bat
   .\run-frontend.bat
   ```
   или ръчно:
   ```bat
   cd frontend
   npm install
   npm run dev
   ```
6. Отвори в браузър: http://localhost:5173/

След преименуване Vite ще работи нормално, защото пътят вече няма да съдържа `&`.
