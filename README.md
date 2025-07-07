## 🔷 TEAM 1 – **User Manager & Profile App**

### 🎯 Fokus: Manajemen user (list, detail, form)

📄 Total Pages: 10

- LoginActivity
- MainActivity
- FragmentUserList
- FragmentUserDetail
- FragmentUserEdit
- FragmentUserAdd
- FragmentSearchUser
- FragmentUserProfile
- FragmentUserStatistics (chart by gender/age)
- FragmentDeletedUser (mock delete)

---

### 📘 API DOCUMENTATION

#### ✅ GET All Users

- **URL**: `GET /users`
- **Query**: `limit`, `skip`
- **Example**: `/users?limit=10`
- **Response**:

```json
{
  "users": [ { "id": 1, "firstName": "John", ... } ],
  "total": 100,
  "limit": 10,
  "skip": 0
}
```

#### ✅ GET User by ID

- **URL**: `GET /users/{id}`
- **Response**:

```json
{
  "id": 1,
  "firstName": "John",
  "email": "john@example.com"
}
```

#### ✅ POST Add User

- **URL**: `POST /users/add`
- **Body**:

```json
{
  "firstName": "Ahmad",
  "lastName": "Fajar",
  "email": "ahmad@example.com",
  "gender": "male"
}
```

#### ✅ PUT Edit User

- **URL**: `PUT /users/{id}`
- **Body**:

```json
{
  "firstName": "Fajar",
  "email": "fajar@example.com"
}
```

---
