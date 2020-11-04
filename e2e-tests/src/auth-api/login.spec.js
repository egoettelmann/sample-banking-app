const chai = require("chai");
const should = chai.should();
const expect = chai.expect;
const app = require('../app')

describe("Login", () => {
    it("should fail for wrong login", done => {
        app.authApi
            .post("/api/login")
            .field({username: 'a', password: 'a'})
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(401);
                res.body.should.be.a("object");
                res.body.should.have.property("title");
                res.body.should.have.property("detail");
                done();
            });
    });
});
