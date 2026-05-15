from fastapi import FastAPI, Depends, HTTPException, status
from fastapi.middleware.cors import CORSMiddleware
from typing import List, Optional
from pydantic import BaseModel
from sqlalchemy import create_engine, Column, Integer, String, DateTime
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, Session
from passlib.context import CryptContext
import uvicorn
import uuid
import datetime
import os

# --- Database Setup ---
# Using environment variable for flexibility
DATABASE_URL = os.getenv("DATABASE_URL", "postgresql://ecoskiller:ecoskillerpassword@postgres:5432/ecoskiller_db")

engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

# --- Tables ---
class User(Base):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True, index=True)
    euid = Column(String, unique=True, index=True)
    name = Column(String)
    email = Column(String, unique=True, index=True)
    hashed_password = Column(String)
    role = Column(String)
    created_at = Column(DateTime, default=datetime.datetime.utcnow)

Base.metadata.create_all(bind=engine)

app = FastAPI(title="EcoSkiller Auth Service v2", version="2.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# --- Schemas ---
class LoginRequest(BaseModel):
    email: str
    password: str

class SignupRequest(BaseModel):
    name: str
    email: str
    password: str
    role: str

# --- Dependency ---
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.get("/")
async def root():
    return {"service": "Auth-Service", "status": "online", "environment": "dev"}

@app.post("/signup")
async def signup(request: SignupRequest, db: Session = Depends(get_db)):
    # Check if user exists
    db_user = db.query(User).filter(User.email == request.email).first()
    if db_user:
        raise HTTPException(status_code=400, detail="Email already registered")
    
    # Generate EUID (EcoSkiller Unified ID)
    # Format: ROLE-REGION-YEAR-RANDOM-X
    role_prefix = request.role[:3].upper()
    random_part = str(uuid.uuid4()).split("-")[0].upper()
    euid = f"{role_prefix}-IN-26-{random_part}-X"

    hashed_password = pwd_context.hash(request.password)
    
    new_user = User(
        euid=euid,
        name=request.name,
        email=request.email,
        hashed_password=hashed_password,
        role=request.role
    )
    db.add(new_user)
    db.commit()
    db.refresh(new_user)
    
    return {
        "status": "success",
        "message": "User registered successfully",
        "euid": euid
    }

@app.post("/login")
async def login(request: LoginRequest, db: Session = Depends(get_db)):
    user = db.query(User).filter(User.email == request.email).first()
    
    if not user or not pwd_context.verify(request.password, user.hashed_password):
        raise HTTPException(status_code=401, detail="Invalid email or password")
        
    return {
        "status": "success",
        "token": f"skiller_token_{user.euid}",
        "user": {
            "name": user.name,
            "email": user.email,
            "role": user.role,
            "euid": user.euid
        }
    }

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
