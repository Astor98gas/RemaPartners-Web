export interface User {
    id: string
    username: string
    email: string
    password: string
    confirmPassword: string
}

export interface UserFormData {
    email: string
    password: string
    confirmPassword: string
}

export interface UserLogin {
    email: string
    password: string
}