const chai = require("chai");
const chaiHttp = require("chai-http");

const AUTH_API_URL = process.env.AUTH_API_URL || "http://localhost:9091";
const BANKING_API_URL = process.env.BANKING_API_URL || "http://localhost:9090";

chai.use(chaiHttp);

const authApi = chai.request.agent(AUTH_API_URL);
const bankingApi = chai.request.agent(BANKING_API_URL);

const users = {
    'user1': {
        credentials: {
            username: 'user1@test.com',
            password: 'password1'
        }
    },
    'user2': {
        credentials: {
            username: 'user2@test.com',
            password: 'password2'
        }
    }
};

module.exports = {
    authApi: authApi,
    bankingApi: bankingApi,
    authHeaders: async function (user = 'user1') {
        if (users[user].auth) {
            return Promise.resolve(users[user].auth);
        }
        return authApi
            .post('/api/login')
            .field(users[user].credentials)
            .then(function (res) {
                users[user].auth = {'Authorization': 'Bearer ' + res.body.token}
                return users[user].auth;
            });
    }
}
