export interface User {
    readonly id: string
    username: string
    email: string
    password: string
    rol: {
        id: number;
        name: string;
    };
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