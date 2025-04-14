export interface User {
    id: string
    username: string
    email: string
    password: string
    confirmPassword: string
}

export interface UserFormData {
    username: string
    email: string
    password: string
    confirmPassword: string
}

export interface UserLogin {
    username: string
    password: string
}